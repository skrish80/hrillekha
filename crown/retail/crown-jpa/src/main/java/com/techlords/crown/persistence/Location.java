package com.techlords.crown.persistence;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * The persistent class for the location database table.
 * 
 */
@Entity
@Table(name = "location")
public class Location implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "LOCATION_LOCATIONID_GENERATOR", sequenceName = "LOCATION_LOCATION_ID_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LOCATION_LOCATIONID_GENERATOR")
	@Column(name = "location_id", unique = true, nullable = false)
	private Integer locationId;

	@Column(length = 100)
	private String description;

	@Column(nullable = false, length = 30)
	private String location;

	// bi-directional many-to-one association to Agent
	@OneToMany(mappedBy = "location")
	private Set<Agent> agents;

	// bi-directional many-to-one association to Company
	@OneToMany(mappedBy = "locationBean")
	private Set<Company> companies;

	// bi-directional many-to-one association to Warehouse
	@OneToMany(mappedBy = "locationBean")
	private Set<Warehouse> warehouses;

	public Location() {
	}

	public Integer getLocationId() {
		return this.locationId;
	}

	public void setLocationId(Integer locationId) {
		this.locationId = locationId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Set<Agent> getAgents() {
		return this.agents;
	}

	public void setAgents(Set<Agent> agents) {
		this.agents = agents;
	}

	public Set<Company> getCompanies() {
		return this.companies;
	}

	public void setCompanies(Set<Company> companies) {
		this.companies = companies;
	}

	public Set<Warehouse> getWarehouses() {
		return this.warehouses;
	}

	public void setWarehouses(Set<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}
	
	@Version
	@Column(name = "version", unique = true, nullable = false)
	private long version;

	public final long getVersion() {
		return version;
	}

	public final void setVersion(long version) {
		this.version = version;
	}

}