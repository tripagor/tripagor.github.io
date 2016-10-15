package com.tripagor.google.api.model;

import java.util.LinkedList;
import java.util.List;

public class Lodging {
	private String name;

	private String description;

	private String url;

	private Address address;

	private List<String> imageUrls = new LinkedList<String>();

	private List<FeatureSection> featureSections = new LinkedList<FeatureSection>();

	private boolean isMarked;

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

	public List<String> getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(List<String> imageUrls) {
		this.imageUrls = imageUrls;
	}

	public List<FeatureSection> getFeatureSections() {
		return featureSections;
	}

	public void setFeatureSections(List<FeatureSection> featureSections) {
		this.featureSections = featureSections;
	}

	public boolean isMarked() {
		return isMarked;
	}

	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}

}
