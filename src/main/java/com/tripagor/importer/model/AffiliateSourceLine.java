package com.tripagor.importer.model;

import org.beanio.annotation.Field;
import org.beanio.annotation.Record;

@Record
public class AffiliateSourceLine {

	@Override
	public String toString() {
		return "AffiliateSourceLine [name=" + name + ", plz=" + plz + ", city=" + city + ", url=" + url + "]";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Field(at = 0)
	private String name;
	@Field(at = 1)
	private String plz;
	@Field(at = 2)
	private String city;
	@Field(at = 3)
	private String url;

}
