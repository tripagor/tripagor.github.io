package com.tripagor.hotels.model;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Document
@JsonInclude(Include.NON_NULL)
@CompoundIndexes({
		@CompoundIndex(name = "place_marker_idex", def = "{'is_evaluated': 1, 'is_marker_set': 1, 'is_marker_approved': 1, 'well_formatted_address': 1, 'place_id': 1}"),
		@CompoundIndex(name = "country_city_idex", def = "{'country_code': 1, 'city_unique': 1}") })
public class Hotel {
	@Id
	private String id;
	private @Indexed @Field("booking_com_id") Long bookingComId;
	@Indexed
	private String name;
	private String address;
	private String zip;
	private @Indexed @Field("city_hotel") String city;
	private @Indexed @Field("country_code") String countryCode;
	private String ufi;
	@Field("class")
	private Integer hotelClass;
	@Field("currencycode")
	private String currencycode;
	private Double minrate;
	private Double maxrate;
	@Field("nr_rooms")
	private Integer nrRooms;
	private String longitude;
	private String latitude;
	@Field("public_ranking")
	private Double publicRanking;
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
	private @Field("city_unique") String cityUnique;
	private @Indexed @Field("city_preferred") String cityPreferred;
	private @Indexed @Field("continent_id") @JsonIgnore Integer continentId;
	@Field("review_score")
	private Double reviewScore;
	@Field("review_nr")
	private Integer reviewNr;
	private @Field("preferred") @JsonIgnore int preferredBookingComPartner;
	@LastModifiedDate
	private Date lastModifiedDate;
	@CreatedDate
	private Date createdDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getBookingComId() {
		return bookingComId;
	}

	public void setBookingComId(Long bookingComId) {
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
		return countryCode.toUpperCase();
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode.toUpperCase();
	}

	public String getUfi() {
		return ufi;
	}

	public void setUfi(String ufi) {
		this.ufi = ufi;
	}

	public Integer getHotelClass() {
		return hotelClass;
	}

	public void setHotelClass(Integer hotelClass) {
		this.hotelClass = hotelClass;
	}

	public String getCurrencycode() {
		return currencycode;
	}

	public void setCurrencycode(String currencycode) {
		this.currencycode = currencycode;
	}

	public Double getMinrate() {
		return minrate;
	}

	public void setMinrate(Double minrate) {
		this.minrate = minrate;
	}

	public Double getMaxrate() {
		return maxrate;
	}

	public void setMaxrate(Double maxrate) {
		this.maxrate = maxrate;
	}

	public Integer getNrRooms() {
		return nrRooms;
	}

	public void setNrRooms(Integer nrRooms) {
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

	public Double getPublicRanking() {
		return publicRanking;
	}

	public void setPublicRanking(Double publicRanking) {
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

	public Double getReviewScore() {
		return reviewScore;
	}

	public void setReviewScore(Double reviewScore) {
		this.reviewScore = reviewScore;
	}

	public Integer getReviewNr() {
		return reviewNr;
	}

	public void setReviewNr(Integer reviewNr) {
		this.reviewNr = reviewNr;
	}

	public WorldRegion getWorldRegion() {
		return WorldRegion.fromValue(this.continentId);
	}

	public void setWorldRegion(WorldRegion worldRegion) {
		this.continentId = worldRegion.toValue();
	}

	public Integer getContinentId() {
		return continentId;
	}

	public void setContinentId(Integer continentId) {
		this.continentId = continentId;
	}

	public int getPreferredBookingComPartner() {
		return preferredBookingComPartner;
	}

	public void setPreferredBookingComPartner(int preferredBookingComPartner) {
		this.preferredBookingComPartner = preferredBookingComPartner;
	}

	public boolean isPreferred() {
		return this.preferredBookingComPartner == 1;
	}

	public void setPreferred(boolean isPreferred) {
		if (isPreferred) {
			this.preferredBookingComPartner = 1;
		} else {
			this.preferredBookingComPartner = 1;
		}
	}

	@Override
	public String toString() {
		return "Hotel [id=" + id + ", bookingComId=" + bookingComId + ", name=" + name + ", address=" + address
				+ ", zip=" + zip + ", city=" + city + ", countryCode=" + countryCode + ", ufi=" + ufi + ", hotelClass="
				+ hotelClass + ", currencycode=" + currencycode + ", minrate=" + minrate + ", maxrate=" + maxrate
				+ ", nrRooms=" + nrRooms + ", longitude=" + longitude + ", latitude=" + latitude + ", publicRanking="
				+ publicRanking + ", url=" + url + ", imageUrl=" + imageUrl + ", descEn=" + descEn + ", descFr="
				+ descFr + ", descEs=" + descEs + ", descDe=" + descDe + ", descNl=" + descNl + ", descIt=" + descIt
				+ ", descPt=" + descPt + ", descJa=" + descJa + ", descZh=" + descZh + ", descPl=" + descPl
				+ ", descRu=" + descRu + ", descSv=" + descSv + ", descAr=" + descAr + ", descEl=" + descEl
				+ ", descNo=" + descNo + ", cityUnique=" + cityUnique + ", cityPreferred=" + cityPreferred
				+ ", continentId=" + continentId + ", reviewScore=" + reviewScore + ", reviewNr=" + reviewNr
				+ ", preferredBookingComPartner=" + preferredBookingComPartner + ", lastModifiedDate="
				+ lastModifiedDate + ", createdDate=" + createdDate + "]";
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
