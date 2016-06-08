
package com.tripagor.google.api.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaceAddRequest {

	@JsonProperty("location")
	private Location location;
	@JsonProperty("accuracy")
	private Integer accuracy;
	@JsonProperty("name")
	private String name;
	@JsonProperty("phone_number")
	private String phoneNumber;
	@JsonProperty("address")
	private String address;
	@JsonProperty("types")
	private List<String> types = new ArrayList<String>();
	@JsonProperty("website")
	private String website;
	@JsonProperty("language")
	private String language;

	/**
	 * 
	 * @return The location
	 */
	@JsonProperty("location")
	public Location getLocation() {
		return location;
	}

	/**
	 * 
	 * @param location
	 *            The location
	 */
	@JsonProperty("location")
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * 
	 * @return The accuracy
	 */
	@JsonProperty("accuracy")
	public Integer getAccuracy() {
		return accuracy;
	}

	/**
	 * 
	 * @param accuracy
	 *            The accuracy
	 */
	@JsonProperty("accuracy")
	public void setAccuracy(Integer accuracy) {
		this.accuracy = accuracy;
	}

	/**
	 * 
	 * @return The name
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name
	 */
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return The phoneNumber
	 */
	@JsonProperty("phone_number")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * 
	 * @param phoneNumber
	 *            The phone_number
	 */
	@JsonProperty("phone_number")
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * 
	 * @return The address
	 */
	@JsonProperty("address")
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * @param address
	 *            The address
	 */
	@JsonProperty("address")
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 
	 * @return The types
	 */
	@JsonProperty("types")
	public List<String> getTypes() {
		return types;
	}

	/**
	 * 
	 * @param types
	 *            The types
	 */
	@JsonProperty("types")
	public void setTypes(List<String> types) {
		this.types = types;
	}

	/**
	 * 
	 * @return The website
	 */
	@JsonProperty("website")
	public String getWebsite() {
		return website;
	}

	/**
	 * 
	 * @param website
	 *            The website
	 */
	@JsonProperty("website")
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * 
	 * @return The language
	 */
	@JsonProperty("language")
	public String getLanguage() {
		return language;
	}

	/**
	 * 
	 * @param language
	 *            The language
	 */
	@JsonProperty("language")
	public void setLanguage(String language) {
		this.language = language;
	}

}
