package com.techlords.crown.license;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <p>
 * This class generates an asymmetric key-pair (public key cryptography). The current time is used
 * as a seed to the secure random algorithm which is used to generate the key bytes. Note that the
 * algorithm used is SHA1PRNG, which is a cryptographically strong, Pseudo-Random Number Generator
 * (PRNG) based on the SHA-1 message digest algorithm.
 * </p>
 * <p>
 * The key length in bits can be specified as a parameter. The main method generates a key pair and
 * writes the pair to disk under the files <code>private-key.bin</code> and
 * <code>public-key.bin</code> to the current working directory.
 * </p>
 * 
 * @author G. Vaidhyanathan
 */
public final class CrownKeyGenerator {

	public static final CrownKeyGenerator INSTANCE = new CrownKeyGenerator();

	private CrownKeyGenerator() {
		// Private C'tor
	}

	/**
	 * Generate an RSA key pair and save it to the file
	 */
	public void genKeys(String keyFile) throws Exception {
		System.out.println("Generating RSA keys");

		KeyFactory factory = KeyFactory.getInstance("RSA");
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
		kpg.initialize(2048);
		KeyPair kp = kpg.generateKeyPair();
		X509EncodedKeySpec pubSpec = factory.getKeySpec(kp.getPublic(), X509EncodedKeySpec.class);
		PKCS8EncodedKeySpec privSpec = factory.getKeySpec(kp.getPrivate(),
				PKCS8EncodedKeySpec.class);

		FileOutputStream pubFile = new FileOutputStream(keyFile + "-pub.bin");
		DataOutputStream pubDat = new DataOutputStream(pubFile);

		byte[] enc = pubSpec.getEncoded();
		pubDat.writeInt(enc.length);
		pubDat.write(enc);
		pubDat.flush();
		pubDat.close();

		FileOutputStream privFile = new FileOutputStream(keyFile + "-priv.bin");
		DataOutputStream privDat = new DataOutputStream(privFile);
		enc = privSpec.getEncoded();
		privDat.writeInt(enc.length);
		privDat.write(enc);
		privDat.flush();
		privDat.close();

		pubFile.close();
		privFile.close();
	}

	public void generateVerificationKey() throws Exception {
		try {
			final String key = "CROWNlicDURATION";
			byte[] raw = key.getBytes(Charset.forName("UTF-8"));
			if (raw.length != 16) {
				throw new IllegalArgumentException("Invalid key size.");
			}

			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));

			long time = System.currentTimeMillis();

			System.out.println("time ::: " + time);
			byte[] inputBytes = ByteBuffer.allocate(256).putLong(time).array();

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

	public void generateLicense(File keyFile, CrownLicense license) throws Exception {
		PublicKey pub = loadPublic(keyFile);
		Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");

		// create new AES key
		KeyGenerator gen = KeyGenerator.getInstance("AES");
		gen.init(256);
		SecretKey key = gen.generateKey();

		// RSA encrypt AES key
		byte[] keyEnc = key.getEncoded();
		rsa.init(Cipher.ENCRYPT_MODE, pub);
		byte[] keySec = rsa.doFinal(keyEnc);

		// Create AES cipher
		Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
		aes.init(Cipher.ENCRYPT_MODE, key);
		byte[] iv = aes.getIV();

		FileOutputStream out = new FileOutputStream(license.getLicensee() + ".lic");
		DataOutputStream dat = new DataOutputStream(out);

		// save encrypted AES key and IV
		dat.writeInt(keySec.length);
		dat.write(keySec);
		dat.writeInt(iv.length);
		dat.write(iv);

		dat.write(aes.doFinal(license.serialize()));
		dat.flush();
		dat.close();
	}

	/**
	 * Load an RSA key pair from the file
	 */
	public PublicKey loadPublic(File keyFile) throws Exception {

		FileInputStream in = new FileInputStream(keyFile);
		DataInputStream dat = new DataInputStream(in);

		int len = dat.readInt();
		byte[] enc = new byte[len];
		dat.readFully(enc);
		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(enc);

		dat.close();
		return KeyFactory.getInstance("RSA").generatePublic(pubSpec);
	}

	/**
	 * Load an RSA key pair from the file
	 */
	public KeyPair loadKeys(File keyFile) throws Exception {
		System.out.println("Loading RSA keys");

		FileInputStream in = new FileInputStream(keyFile);
		DataInputStream dat = new DataInputStream(in);

		int len = dat.readInt();
		byte[] enc = new byte[len];
		dat.readFully(enc);
		X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(enc);

		len = dat.readInt();
		enc = new byte[len];
		dat.readFully(enc);
		PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(enc);

		dat.close();

		KeyFactory factory = KeyFactory.getInstance("RSA");
		PublicKey pub = factory.generatePublic(pubSpec);
		PrivateKey priv = factory.generatePrivate(privSpec);

		System.out.println("RSA keys loaded from " + keyFile.getPath());
		return new KeyPair(pub, priv);
	}

	public static void main(String[] args) throws Exception {
//		 INSTANCE.genKeys("crown-keypair");
//		 INSTANCE.generateLicense(new File("crown-keypair-pub.bin"), new CrownLicense());
//		INSTANCE.generateVerificationKey();
	}
}