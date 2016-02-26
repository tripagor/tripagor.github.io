package com.tripagor.importer.model;

import org.beanio.annotation.Record;

@Record
public class Accommodation {
	private String name;

	private String description;

	private String url;

	private Address address;

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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Accommodation [title=" + name + ", description=" + description + ", url=" + url + ", address=" + address
				+ "]";
	}

}
