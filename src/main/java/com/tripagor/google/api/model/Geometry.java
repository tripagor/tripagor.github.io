
package com.tripagor.model;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "location", "location_type", "viewport" })
public class Geometry {

	@JsonProperty("location")
	private Location location;
	@JsonProperty("location_type")
	private String locationType;
	@JsonProperty("viewport")
	private Viewport viewport;

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
	 * @return The locationType
	 */
	@JsonProperty("location_type")
	public String getLocationType() {
		return locationType;
	}

	/**
	 * 
	 * @param locationType
	 *            The location_type
	 */
	@JsonProperty("location_type")
	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	/**
	 * 
	 * @return The viewport
	 */
	@JsonProperty("viewport")
	public Viewport getViewport() {
		return viewport;
	}

	/**
	 * 
	 * @param viewport
	 *            The viewport
	 */
	@JsonProperty("viewport")
	public void setViewport(Viewport viewport) {
		this.viewport = viewport;
	}

}
