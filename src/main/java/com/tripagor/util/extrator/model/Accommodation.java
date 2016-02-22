package com.tripagor.util.extrator.model;

public class Accommodation {
	@Override
	public String toString() {
		return "Accommodation [title=" + title + ", description=" + description + ", url=" + url + ", address="
				+ address + "]";
	}

	private String title;
	private String description;
	private String url;
	private String address;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
