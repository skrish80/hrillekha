package com.techlords.infra;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * <code>AppModel</code> class. It is a base class for all the Model classes
 * 
 * @author G. Vaidhyanathan
 * @version 1.0
 */
@SuppressWarnings("serial")
public class AppModel implements Serializable, Comparable<AppModel> {
	public static final AppModel DUMMY_MODEL = new AppModel();
	private Integer id;
	private long version = 1L;

	@Override
	public String toString() {
		if (this == DUMMY_MODEL) {
			return ""; //$NON-NLS-1$
		}
		return super.toString();
	}

	@Override
	public int compareTo(AppModel o) {
		return 0;
	}

	@JsonIgnore
	public boolean isNew() {
		return (id == null);
	}

	/**
	 * @return the id
	 */
	public final Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public final void setId(Integer id) {
		this.id = id;
	}

	public final long getVersion() {
		return version;
	}

	public final void setVersion(long version) {
		this.version = version;
	}
}
