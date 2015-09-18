/**
 * 
 */
package com.techlords.crown.helpers;

import com.techlords.crown.business.model.LocationBO;
import com.techlords.crown.persistence.Location;

/**
 * @author gv
 * 
 */
public final class LocationHelper {

	public LocationBO createLocationBO(Location location) {
		LocationBO bo = new LocationBO();
		bo.setId(location.getLocationId());
		bo.setVersion(location.getVersion());
		bo.setLocation(location.getLocation());
		bo.setDescription(location.getDescription());
		return bo;
	}

	public Location createLocation(LocationBO locationBO) {
		Location location = new Location();
		location.setLocationId(locationBO.getId());
		location.setVersion(locationBO.getVersion());
		location.setLocation(locationBO.getLocation());
		location.setDescription(locationBO.getDescription());
		return location;
	}
}
