package com.tripagor.importer.model;

import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

@Record
public class Accommodation {
	@Override
	public String toString() {
		return "Accommodation [title=" + name + ", description=" + description + ", url=" + url + ", address="
				+ address + "]";
	}

	@Field(at = 0)
	private String name;

	@Field(at = 1)
	private String description;

	@Field(at = 2)
	private String url;

	@Field(at = 3)
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String title) {
		this.name = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
