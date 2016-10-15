package com.tripagor.google.api.model;

import java.util.LinkedList;
import java.util.List;

public class FeatureSection {
	private String name;
	private List<String> features = new LinkedList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFeatures() {
		return features;
	}

	public void setFeatures(List<String> features) {
		this.features = features;
	}

}
