
package com.tripagor.importer.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "types", "formatted_address", "website", "name", "location", "phoneNumber", "accuracy" })
public class Place {

	@JsonProperty("types")
	private List<String> types = new ArrayList<String>();
	@JsonProperty("formatted_address")
	private String formattedAddress;
	@JsonProperty("website")
	private String website;
	@JsonProperty("name")
	private String name;
	@JsonProperty("accuracy")
	private int accuracy;
	@JsonProperty("phone_number")
	private String phoneNumber;
	@JsonProperty("location")
	private Location location;

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
	 * @return The formattedAddress
	 */
	@JsonProperty("formatted_address")
	public String getFormattedAddress() {
		return formattedAddress;
	}

	/**
	 * 
	 * @param formattedAddress
	 *            The formatted_address
	 */
	@JsonProperty("formatted_address")
	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
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

	@JsonProperty("accuracy")
	public int getAccuracy() {
		return accuracy;
	}

	@JsonProperty("accuracy")
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}

	@JsonProperty("phone_number")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	@JsonProperty("phone_number")
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

}
