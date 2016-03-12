package com.tripagor.hotels.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

public class Hotel {
	@Id
	private String id;

	@Field("booking_com_id")
	private long bookingComId;
	private String name;
	private String address;
	private String zip;
	@Field("city_hotel")
	private String city;
	@Field("country_code")
	private String countryCode;
	private String ufi;
	@Field("class")
	private double hotelClass;
	@Field("currencycode")
	private String currencycode;
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
	@Field("desc_fr")
	private String descFr;
	@Field("desc_es")
	private String descEs;
	@Field("desc_de")
	private String descDe;
	@Field("desc_nl")
	private String descNl;
	@Field("desc_it")
	private String descIt;
	@Field("desc_pt")
	private String descPt;
	@Field("desc_ja")
	private String descJa;
	@Field("desc_zh")
	private String descZh;
	@Field("desc_pl")
	private String descPl;
	@Field("desc_ru")
	private String descRu;
	@Field("desc_sv")
	private String descSv;
	@Field("desc_ar")
	private String descAr;
	@Field("desc_el")
	private String descEl;
	@Field("desc_no")
	private String descNo;
	@Field("city_unique")
	private String cityUnique;
	@Field("city_preferred")
	private String cityPreferred;
	@Field("continent_id")
	private int continentId;
	@Field("review_score")
	private double reviewScore;
	@Field("review_nr")
	private int reviewNr;
	@Field("is_evaluated")
	private boolean isEvaluated;
	@Field("is_marker_set")
	private boolean isMarkerSet;
	@Field("is_marker_approved")
	private boolean isMarkerApproved;
	@Field("well_formatted_address")
	private String formattedAddress;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getBookingComId() {
		return bookingComId;
	}

	public void setBookingComId(long bookingComId) {
		this.bookingComId = bookingComId;
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

	public double getHotelClass() {
		return hotelClass;
	}

	public void setHotelClass(double hotelClass) {
		this.hotelClass = hotelClass;
	}

	public String getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
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

	public int getNrRooms() {
		return nrRooms;
	}

	public void setNrRooms(int nrRooms) {
		this.nrRooms = nrRooms;
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

	public String getDescFr() {
		return descFr;
	}

	public void setDescFr(String descFr) {
		this.descFr = descFr;
	}

	public String getDescEs() {
		return descEs;
	}

	public void setDescEs(String descEs) {
		this.descEs = descEs;
	}

	public String getDescDe() {
		return descDe;
	}

	public void setDescDe(String descDe) {
		this.descDe = descDe;
	}

	public String getDescNl() {
		return descNl;
	}

	public void setDescNl(String descNl) {
		this.descNl = descNl;
	}

	public String getDescIt() {
		return descIt;
	}

	public void setDescIt(String descIt) {
		this.descIt = descIt;
	}

	public String getDescPt() {
		return descPt;
	}

	public void setDescPt(String descPt) {
		this.descPt = descPt;
	}

	public String getDescJa() {
		return descJa;
	}

	public void setDescJa(String descJa) {
		this.descJa = descJa;
	}

	public String getDescZh() {
		return descZh;
	}

	public void setDescZh(String descZh) {
		this.descZh = descZh;
	}

	public String getDescPl() {
		return descPl;
	}

	public void setDescPl(String descPl) {
		this.descPl = descPl;
	}

	public String getDescRu() {
		return descRu;
	}

	public void setDescRu(String descRu) {
		this.descRu = descRu;
	}

	public String getDescSv() {
		return descSv;
	}

	public void setDescSv(String descSv) {
		this.descSv = descSv;
	}

	public String getDescAr() {
		return descAr;
	}

	public void setDescAr(String descAr) {
		this.descAr = descAr;
	}

	public String getDescEl() {
		return descEl;
	}

	public void setDescEl(String descEl) {
		this.descEl = descEl;
	}

	public String getDescNo() {
		return descNo;
	}

	public void setDescNo(String descNo) {
		this.descNo = descNo;
	}

	public String getCityUnique() {
		return cityUnique;
	}

	public void setCityUnique(String cityUnique) {
		this.cityUnique = cityUnique;
	}

	public String getCityPreferred() {
		return cityPreferred;
	}

	public void setCityPreferred(String cityPreferred) {
		this.cityPreferred = cityPreferred;
	}

	public int getContinentId() {
		return continentId;
	}

	public void setContinentId(int continentId) {
		this.continentId = continentId;
	}

	public double getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(double reviewScore) {
		this.reviewScore = reviewScore;
	}

	public int getReviewNr() {
		return reviewNr;
	}

	public void setReviewNr(int reviewNr) {
		this.reviewNr = reviewNr;
	}

	public boolean isEvaluated() {
		return isEvaluated;
	}

	public void setEvaluated(boolean isEvaluated) {
		this.isEvaluated = isEvaluated;
	}

	public boolean isMarkerSet() {
		return isMarkerSet;
	}

	public void setMarkerSet(boolean isMarkerSet) {
		this.isMarkerSet = isMarkerSet;
	}

	public boolean isMarkerApproved() {
		return isMarkerApproved;
	}

	public void setMarkerApproved(boolean isMarkerApproved) {
		this.isMarkerApproved = isMarkerApproved;
	}

	public String getFormattedAddress() {
		return formattedAddress;
	}

	public void setFormattedAddress(String formattedAddress) {
		this.formattedAddress = formattedAddress;
	}

}
