package com.tripagor.locations;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@CompoundIndexes({ @CompoundIndex(name = "region_country", unique = true, def = "{'name' : 1, 'countryCode' : 1}") })
@Document
public class Region {

	@Id
	private String id;
	private String name;
	private int numOfHotels;
	private String countryCode;
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumOfHotels() {
		return numOfHotels;
	}

	public void setNumOfHotels(int numOfHotels) {
		this.numOfHotels = numOfHotels;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
