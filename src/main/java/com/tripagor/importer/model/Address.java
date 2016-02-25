package com.tripagor.importer.model;

public class Address {
	private String street;
	private String zip;
	private String city;
	private String country;
	private String suburb;
	private String streetNumber;
	

	public Address(String street, String zip, String city, String country, String suburb, String streetNumber) {
		super();
		this.street = street;
		this.zip = zip;
		this.city = city;
		this.country = country;
		this.suburb = suburb;
		this.streetNumber = streetNumber;
	}

	public Address(String text) {
		try {
			String[] addressParts = text.split(",");
			if (addressParts.length == 3) {
				this.street = addressParts[0].trim();
				this.city = addressParts[1].trim();
				this.country = addressParts[2].trim();
			} else if(addressParts.length == 4){
				this.street = addressParts[0].trim();
				this.suburb = addressParts[1].trim();
				this.city = addressParts[2].trim();
				this.country = addressParts[3].trim();
			}
		} catch (Exception e) {
			throw new RuntimeException(new IllegalArgumentException("addressLine is invalid"));
		}
	}

	public Address() {

	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
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

	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	@Override
	public String toString() {
		return "Address [street=" + street + ", zip=" + zip + ", city=" + city + ", country=" + country + ", suburb="
				+ suburb + ", streetNumber=" + streetNumber + "]";
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

}
