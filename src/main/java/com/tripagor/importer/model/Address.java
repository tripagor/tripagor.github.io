package com.tripagor.importer.model;

public class Address {
	private String street;
	private String postalCode;
	private String city;
	private String country;
	private String streetNumber;

	public Address(String street, String postalCode, String city, String country, String streetNumber) {
		super();
		this.street = street;
		this.postalCode = postalCode;
		this.city = city;
		this.country = country;
		this.streetNumber = streetNumber;
	}

	public Address() {

	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String zip) {
		this.postalCode = zip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	@Override
	public String toString() {
		return "Address [street=" + street + ", postalCode=" + postalCode + ", city=" + city + ", country=" + country
				+ ", streetNumber=" + streetNumber + "]";
	}

}
