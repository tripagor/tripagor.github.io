
package com.tripagor.google.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "long_name", "short_name", "types" })
public class AddressComponent {

	@JsonProperty("long_name")
	private String longName;
	@JsonProperty("short_name")
	private String shortName;
	@JsonProperty("types")
	private List<String> types = new ArrayList<String>();

	/**
	 * 
	 * @return The longName
	 */
	@JsonProperty("long_name")
	public String getLongName() {
		return longName;
	}

	/**
	 * 
	 * @param longName
	 *            The long_name
	 */
	@JsonProperty("long_name")
	public void setLongName(String longName) {
		this.longName = longName;
	}

	/**
	 * 
	 * @return The shortName
	 */
	@JsonProperty("short_name")
	public String getShortName() {
		return shortName;
	}

	/**
	 * 
	 * @param shortName
	 *            The short_name
	 */
	@JsonProperty("short_name")
	public void setShortName(String shortName) {
		this.shortName = shortName;
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
