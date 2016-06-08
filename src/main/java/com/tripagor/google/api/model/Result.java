
package com.tripagor.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "address_components", "formatted_address", "geometry", "place_id", "types" })
public class Result {

	@JsonProperty("address_components")
	private List<AddressComponent> addressComponents = new ArrayList<AddressComponent>();
	@JsonProperty("formatted_address")
	private String formattedAddress;
	@JsonProperty("geometry")
	private Geometry geometry;
	@JsonProperty("place_id")
	private String placeId;
	@JsonProperty("types")
	private List<String> types = new ArrayList<String>();

	/**
	 * 
	 * @return The addressComponents
	 */
	@JsonProperty("address_components")
	public List<AddressComponent> getAddressComponents() {
		return addressComponents;
	}

	/**
	 * 
	 * @param addressComponents
	 *            The address_components
	 */
	@JsonProperty("address_components")
	public void setAddressComponents(List<AddressComponent> addressComponents) {
		this.addressComponents = addressComponents;
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
	 * @return The geometry
	 */
	@JsonProperty("geometry")
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * 
	 * @param geometry
	 *            The geometry
	 */
	@JsonProperty("geometry")
	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
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

}
