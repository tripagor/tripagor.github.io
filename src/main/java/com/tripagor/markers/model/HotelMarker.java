package com.tripagor.markers.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelMarker {
	@Id
	private String id;
	private Location location;
	private String name;
	private String phoneNumber;
	private String address;
	private String website;
	private String reference;
	private String placeId;
	private Scope scope;
	@Field("is_owned")
	private Boolean isOwned;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public Scope getScope() {
		return scope;
	}

	public void setScope(Scope scope) {
		this.scope = scope;
	}

	public Boolean getIsOwned() {
		return isOwned;
	}

	public void setIsOwned(Boolean isOwned) {
		this.isOwned = isOwned;
	}

}
