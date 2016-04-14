package com.tripagor.markers.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document
@JsonIgnoreProperties(ignoreUnknown=true)
public class HotelMarkerCheck {
	@Id
	private String id;
	@LastModifiedDate
	private Date lastModifiedDate;
	@CreatedDate
	private Date creationDate;
	private Collection<Approval> approvals = Collections.emptyList();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Collection<Approval> getApprovals() {
		return approvals;
	}
	public void setApprovals(Collection<Approval> approvals) {
		this.approvals = approvals;
	}
	
	

}
