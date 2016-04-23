package com.tripagor.locations;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@CompoundIndexes({ @CompoundIndex(name = "city_county", unique = true, def = "{'name' : 1, 'countryCode' : 1}") })
@Document
public class City {
	@Id
	private String id;
	private String name;
	private String countryCode;
	private int numOfHotels;

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

}
