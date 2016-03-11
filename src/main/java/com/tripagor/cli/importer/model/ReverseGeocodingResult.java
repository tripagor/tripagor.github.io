
package com.tripagor.cli.importer.model;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "results" })
public class ReverseGeocodingResult {

	@JsonProperty("results")
	private List<Result> results = new ArrayList<Result>();

	/**
	 * 
	 * @return The results
	 */
	@JsonProperty("results")
	public List<Result> getResults() {
		return results;
	}

	/**
	 * 
	 * @param results
	 *            The results
	 */
	@JsonProperty("results")
	public void setResults(List<Result> results) {
		this.results = results;
	}

}
