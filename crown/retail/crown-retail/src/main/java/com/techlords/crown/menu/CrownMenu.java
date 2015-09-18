package com.techlords.crown.menu;

import java.io.Serializable;

import com.techlords.crown.mvc.auth.CrownUserDetailsService;

@SuppressWarnings("serial")
public class CrownMenu implements Serializable {

	private final String imageURL;
	private final String linkURL;
	private final String description;

	public CrownMenu(String imgURL, String link, String desc) {
		this.imageURL = imgURL;
		this.linkURL = link;
		this.description = desc;
	}

	/**
	 * @return the imageURL
	 */
	public String getImageURL() {
		return imageURL;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the linkURL
	 */
	public String getLinkURL() {
		return linkURL;
	}

	public boolean isDisplayable() {
		return CrownUserDetailsService.isLinkAllowed(linkURL);
	}
}
