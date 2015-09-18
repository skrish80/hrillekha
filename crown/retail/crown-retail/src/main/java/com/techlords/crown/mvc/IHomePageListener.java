/**
 * 
 */
package com.techlords.crown.mvc;

import java.io.Serializable;
import java.util.EventListener;

/**
 * Notified when the home page is clicked
 * 
 * @author gv
 * 
 */
public interface IHomePageListener extends EventListener, Serializable {
	void homePageSelected();
}
