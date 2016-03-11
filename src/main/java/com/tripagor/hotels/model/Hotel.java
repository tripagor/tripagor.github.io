package com.tripagor.hotels.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class Hotel {
	@Id
	private String id;
	@Field("booking_com_id")
	private int bookingComId;
	@Field("country_code")
	private String countryCode;
	private String ufi;
	@Field("class")
	private String hotelClass;
	@Field("currencycode")
	private String currencyCode;
	private double minrate;
	private double maxrate;
	private String preferred;
	@Field("nr_rooms")
	private int nrRooms;
	private String longitude;
	private String latitude;
	@Field("public_ranking")
	private double publicRanking;
	@Field("hotel_url")
	private String url;
	@Field("photo_url")
	private String imageUrl;
	@Field("desc_en")
	private String descEn;

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

	public int getBookingComId() {
		return bookingComId;
	}

	public void setBookingComId(int bookingComId) {
		this.bookingComId = bookingComId;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getUfi() {
		return ufi;
	}

	public void setUfi(String ufi) {
		this.ufi = ufi;
	}

	public String getHotelClass() {
		return hotelClass;
	}

	public void setHotelClass(String hotelClass) {
		this.hotelClass = hotelClass;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public double getMinrate() {
		return minrate;
	}

	public void setMinrate(double minrate) {
		this.minrate = minrate;
	}

	public double getMaxrate() {
		return maxrate;
	}

	public void setMaxrate(double maxrate) {
		this.maxrate = maxrate;
	}

	public String getPreferred() {
		return preferred;
	}

	public void setPreferred(String preferred) {
		this.preferred = preferred;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public double getPublicRanking() {
		return publicRanking;
	}

	public void setPublicRanking(double publicRanking) {
		this.publicRanking = publicRanking;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDescEn() {
		return descEn;
	}

	public void setDescEn(String descEn) {
		this.descEn = descEn;
	}

	public int getNrRooms() {
		return nrRooms;
	}

	public void setNrRooms(int nrRooms) {
		this.nrRooms = nrRooms;
	}

}
