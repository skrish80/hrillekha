/**
 * 
 */
package com.techlords.crown.service;

import java.util.List;

import com.techlords.crown.business.model.LocationBO;

/**
 * @author gv
 * 
 */
public interface LocationService extends CrownService {

	/**
	 * adds a Location to the application
	 */
	public boolean createLocation(LocationBO locationBO, int userID);

	public boolean updateLocation(LocationBO LocationBO, int userID);

	public boolean deleteLocation(LocationBO LocationBO, int userID);

	public List<LocationBO> findAllLocations();
}
