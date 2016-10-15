
package com.tripagor.google.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PlaceAddResponse {

	@JsonProperty("status")
	private String status;
	@JsonProperty("place_id")
	private String placeId;
	@JsonProperty("scope")
	private String scope;
	@JsonProperty("reference")
	private String reference;
	@JsonProperty("id")
	private String id;

	/**
	 * 
	 * @return The status
	 */
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	/**
	 * 
	 * @param status
	 *            The status
	 */
	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

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

	/**
	 * 
	 * @return The scope
	 */
	@JsonProperty("scope")
	public String getScope() {
		return scope;
	}

	/**
	 * 
	 * @param scope
	 *            The scope
	 */
	@JsonProperty("scope")
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * 
	 * @return The reference
	 */
	@JsonProperty("reference")
	public String getReference() {
		return reference;
	}

	/**
	 * 
	 * @param reference
	 *            The reference
	 */
	@JsonProperty("reference")
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

}