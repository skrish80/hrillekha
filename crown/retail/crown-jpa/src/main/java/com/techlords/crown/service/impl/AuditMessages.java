/**
 * 
 */
package com.techlords.crown.service.impl;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author gv
 * 
 */
final class AuditMessages {

	// The bundle name.
	private static final String BUNDLE_NAME = "com.techlords.crown.service.impl.audit-messages"; //$NON-NLS-1$
	// The resource bundle.
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	// Constructs the messages handler.
	private AuditMessages() {
		// Do nothing.
	}

	/**
	 * Gets the string from the specified key.
	 * 
	 * @param key_p
	 *            The key.
	 * @return The string result.
	 */
	public static String getString(String key_p) {

		try {
			return RESOURCE_BUNDLE.getString(key_p);
		} catch (MissingResourceException e) {
			return '!' + key_p + '!';
		}
	}
}
