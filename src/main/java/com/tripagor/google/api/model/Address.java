package com.tripagor.google.api.model;

public class Address {
	private String streetName;
	private String postalCode;
	private String city;
	private String country;
	private String streetNumber;
	private double latitude;
	private double longitude;
	private String wellFormattedAddress;

	public Address(String streetName, String postalCode, String city, String country, String streetNumber,
			double longitude, double latitude) {
		super();
		this.streetName = streetName;
		this.postalCode = postalCode;
		this.city = city;
		this.country = country;
		this.streetNumber = streetNumber;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public Address() {

	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String street) {
		this.streetName = street;
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
		return "Address [streetName=" + streetName + ", postalCode=" + postalCode + ", city=" + city + ", country="
				+ country + ", streetNumber=" + streetNumber + ", latitude=" + latitude + ", longitude=" + longitude
				+ ", wellFormattedAddress=" + wellFormattedAddress + "]";
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getWellFormattedAddress() {
		return wellFormattedAddress;
	}

	public void setWellFormattedAddress(String wellFormattedAddress) {
		this.wellFormattedAddress = wellFormattedAddress;
	}

}
