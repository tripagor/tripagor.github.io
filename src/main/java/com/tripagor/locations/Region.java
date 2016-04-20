package com.tripagor.locations;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Region {

	@Id
	private String id;
	private String name;
	private int numOfHotels;

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

}
