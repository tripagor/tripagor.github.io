package com.tripagor.cli.importer.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "place_id" })
public class PlaceDeleteRequest {

	@JsonProperty("place_id")
	private String placeId;

	/**
	 * 
	 * @return The placeId
	 */
	@JsonProperty("place_id")
	public String getPlaceId() {
		return placeId;
	}

	/**
	 * 
	 * @param placeId
	 *            The place_id
	 */
	@JsonProperty("place_id")
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

}