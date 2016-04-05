package com.tripagor.cli.service;

import com.tripagor.model.PlaceAddRequest;
import com.tripagor.model.PlaceAddResponse;

public class PlaceAddApiSeleniumImpl implements PlaceAddApi{
	private String username;
	private String password;

	public PlaceAddApiSeleniumImpl(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	@Override
	public PlaceAddResponse add(PlaceAddRequest place) {
		// TODO Auto-generated method stub
		return null;
	}

}
