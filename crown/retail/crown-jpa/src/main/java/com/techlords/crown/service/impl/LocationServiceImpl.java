/**
 * 
 */
package com.techlords.crown.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.techlords.crown.business.model.LocationBO;
import com.techlords.crown.helpers.LocationHelper;
import com.techlords.crown.persistence.Location;
import com.techlords.crown.service.LocationService;

/**
 * @author gv
 * 
 */
@SuppressWarnings("serial")
final class LocationServiceImpl extends AbstractCrownService implements LocationService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.LocationService#addLocation(com.techlords
	 * .crown.business.model.LocationBO)
	 */
	@Override
	public boolean createLocation(LocationBO locationBO, int userID) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.LocationService#editLocation(com.techlords
	 * .crown.business.model.LocationBO)
	 */
	@Override
	public boolean updateLocation(LocationBO LocationBO, int userID) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.LocationService#removeLocation(com.techlords
	 * .crown.business.model.LocationBO)
	 */
	@Override
	public boolean deleteLocation(LocationBO LocationBO, int userID) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.techlords.crown.service.LocationService#findAllLocations()
	 */
	@Override
	public List<LocationBO> findAllLocations() {
		@SuppressWarnings("unchecked")
		final List<Location> locations = manager.createQuery(
				"select l from Location l order by l.location").getResultList();
		final List<LocationBO> bos = new ArrayList<LocationBO>();
		final LocationHelper helper = new LocationHelper();
		for (final Location loc : locations) {
			bos.add(helper.createLocationBO(loc));
		}
		return bos;
	}
}
