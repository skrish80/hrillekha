package com.techlords.crown.license;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Signature Verifier verifies a digital signature. The signature verification requires the
 * private-key part of an asymmetric key pair.
 * 
 * @author GV
 */
public class SignatureVerifier {

	/**
	 * Load an RSA key pair from the file
	 */
	private PrivateKey loadPrivate() throws Exception {

		FileInputStream in = null;
		try {
			in = new FileInputStream(new File("crown-keypair-priv.bin"));
		} catch (Exception e) {
			throw new FileNotFoundException("Key File not found in class path!");
		}
		DataInputStream dat = new DataInputStream(in);

		int len = dat.readInt();
		byte[] enc = new byte[len];
		dat.readFully(enc);
		PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(enc);

		dat.close();
		in.close();

		return KeyFactory.getInstance("RSA").generatePrivate(privSpec);
	}

	public void verifyTimestamp(CrownLicense license) throws Exception {
		long timestamp = getEncryptedTime();
		System.err.println("Timestamp ::: " + timestamp);
		long currentTime = System.currentTimeMillis();

		if (currentTime < timestamp) {
			throw new Exception("System clock is adjusted. Please correct that");
		}
		updateTimestamp(currentTime);
		
		Instant original = Instant.ofEpochMilli(license.getStartDate().getTime());
		Instant now = Instant.ofEpochMilli(currentTime);

		Duration between = Duration.between(original, now);
		long duration = between.abs().toDays();
		if(duration > license.getValidity()) {
			throw new Exception("License expired!");
		}
		System.out.println("Duration ::: " + between.abs().toDays());
	}

	private void updateTimestamp(long currentTime) throws Exception {
		try {
			Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);

			byte[] inputBytes = ByteBuffer.allocate(256).putLong(currentTime).array();

			byte[] outputBytes = cipher.doFinal(inputBytes);

			FileOutputStream outputStream = new FileOutputStream(System.getProperty("user.home")
					+ "/.crown-duration");
			outputStream.write(outputBytes);

			outputStream.close();

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException
				| BadPaddingException | IllegalBlockSizeException | IOException ex) {
			throw new Exception("Error encrypting/decrypting file", ex);
		}
	}

	private Cipher getCipher(int mode) throws Exception {
		final String key = "CROWNlicDURATION";
		byte[] raw = key.getBytes(Charset.forName("UTF-8"));
		if (raw.length != 16) {
			throw new IllegalArgumentException("Invalid key size.");
		}
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(mode, skeySpec, new IvParameterSpec(new byte[16]));
		return cipher;
	}

	private long getEncryptedTime() throws Exception {
		try {

			Cipher cipher = getCipher(Cipher.DECRYPT_MODE);

			FileInputStream in = new FileInputStream(System.getProperty("user.home")
					+ "/.crown-duration");
			DataInputStream dat = new DataInputStream(in);
			// read RSA encrypted AES key
			int len = dat.available();
			byte[] encrypted = new byte[len];
			dat.readFully(encrypted);
			dat.close();
			in.close();

			byte[] original = cipher.doFinal(encrypted);
			return ByteBuffer.wrap(original).getLong();

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException
				| BadPaddingException | IllegalBlockSizeException | IOException ex) {
			throw new Exception("Error encrypting/decrypting file", ex);
		}
	}

	public void verifyLicense(File licenseFile) throws Exception {
		PrivateKey priv = loadPrivate();

		Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");

		FileInputStream in = new FileInputStream(licenseFile);
		DataInputStream dat = new DataInputStream(in);

		// read RSA encrypted AES key
		int len = dat.readInt();
		byte[] buf = new byte[len];
		dat.readFully(buf);
		rsa.init(Cipher.DECRYPT_MODE, priv);

		SecretKeySpec key = new SecretKeySpec(rsa.doFinal(buf), "AES");

		// read the AES IV
		len = dat.readInt();
		buf = new byte[len];
		dat.readFully(buf);
		IvParameterSpec iv = new IvParameterSpec(buf);

		Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
		aes.init(Cipher.DECRYPT_MODE, key, iv);

		// process the rest of the file to get the original back

		buf = new byte[dat.available()];
		dat.readFully(buf);
		byte[] sec = aes.doFinal(buf);
		in.close();

		CrownLicense lic = CrownLicense.deserialize(sec);
		System.out.println(lic.getLicensee());
		System.out.println(lic.getStartDate());
		System.out.println(lic.getValidity());
		verifyTimestamp(lic);
	}

	public static void main(String[] args) throws Exception {
		new SignatureVerifier().verifyLicense(new File("Crown Licensee.lic"));
	}

}