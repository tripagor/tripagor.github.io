package com.tripagor.hotels.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class Hotel {
	@Id
	private String id;
	private int  bookingComId;
	private String countryCode;
	private String uif;
	private String hotelClass;
	private String currencyCode;
	private double minrate;
	private double maxrate;
	private String preferred;
	private byte nrRooms;
	private String longitude;
	private String latitude;
	private double publicRanking;
	private String url;
	private String imageUrl;
	
	private String name;
	private String address;
	@Field("city_hotel")
	private String city;
	

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

}
