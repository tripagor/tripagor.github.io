package com.tripagor.markers.model;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tripagor.hotels.model.Hotel;

@Document
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotelMarkerExport {

	@Id
	private String id;
	public int numberToMark;
	@CreatedDate
	private Date creationDate;
	@LastModifiedDate
	private Date lastModified;
	private Collection<Hotel> hotels = new LinkedList<>();
	private ProcessingStatus status;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumberToMark() {
		return numberToMark;
	}

	public void setNumberToMark(int numberToMark) {
		this.numberToMark = numberToMark;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public Collection<Hotel> getHotels() {
		return hotels;
	}

	public void setHotels(Collection<Hotel> hotels) {
		this.hotels = hotels;
	}

	public ProcessingStatus getStatus() {
		return status;
	}

	public void setStatus(ProcessingStatus status) {
		this.status = status;
	}
}
