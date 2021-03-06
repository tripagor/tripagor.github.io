
package com.tripagor.google.api.model;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "lat", "lng" })
public class Location {

	@JsonProperty("lat")
	private Double lat;

	public Location(Double lat, Double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}

	@JsonProperty("lng")
	private Double lng;

	public Location() {

	}

	/**
	 * 
	 * @return The lat
	 */
	@JsonProperty("lat")
	public Double getLat() {
		return lat;
	}

	/**
	 * 
	 * @param lat
	 *            The lat
	 */
	@JsonProperty("lat")
	public void setLat(Double lat) {
		this.lat = lat;
	}

	/**
	 * 
	 * @return The lng
	 */
	@JsonProperty("lng")
	public Double getLng() {
		return lng;
	}

	/**
	 * 
	 * @param lng
	 *            The lng
	 */
	@JsonProperty("lng")
	public void setLng(Double lng) {
		this.lng = lng;
	}

}
