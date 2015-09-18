package com.techlords.crown.license;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * @author G. Vaidhyanathan
 *
 */
public final class CrownLicense implements Serializable {
	private static final long serialVersionUID = -8061016915711885723L;
	private static final long DEFAULT_VALIDITY = 90; // 90 days

	private String licensee;
	private Date startDate;
	private long validity;

	public CrownLicense() {
		this("Crown Licensee", new Date(), DEFAULT_VALIDITY);
	}

	/**
	 * @param licensee
	 * @param date
	 * @param validity
	 */
	public CrownLicense(String licensee, Date date, long validity) {
		setLicensee(licensee);
		setStartDate(date);
		setValidity(validity);
	}

	/**
	 * @return the licensee
	 */
	public String getLicensee() {
		return licensee;
	}

	/**
	 * @param licensee
	 *            the licensee to set
	 */
	public void setLicensee(String licensee) {
		this.licensee = licensee;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate
	 *            the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the validity
	 */
	public long getValidity() {
		return validity;
	}

	/**
	 * @param validity
	 *            the validity to set
	 */
	public void setValidity(long validity) {
		this.validity = validity;
	}
	
	public byte[] serialize() throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(this);
        o.flush();
        o.close();
        System.err.println("Count ::: " + b.size());
        return b.toByteArray();
    }

    public static CrownLicense deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (CrownLicense) o.readObject();
    }

}
