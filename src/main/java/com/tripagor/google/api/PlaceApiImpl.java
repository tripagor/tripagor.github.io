package com.tripagor.google.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.tripagor.google.api.model.PlaceAddRequest;
import com.tripagor.google.api.model.PlaceAddResponse;
import com.tripagor.google.api.model.PlaceDeleteRequest;
import com.tripagor.google.api.model.PlaceDeleteResponse;

@Component
public class PlaceApiImpl implements PlaceAddApi, PlaceDeleteApi {

	private final static String GOOGLE_MAPS_API_BASE_URL = "https://maps.googleapis.com/maps/api/";
	private final static String GOOGLE_PLACES_API_ADD_PLACE_URL = GOOGLE_MAPS_API_BASE_URL + "place/add/json";
	private final static String GOOGLE_PLACES_API_DELETE_PLACE_URL = GOOGLE_MAPS_API_BASE_URL + "place/delete/json";
	private final String addPlaceApiUrl;
	private String deletePlaceApiUrl;
	private RestTemplate restTemplate;

	@Autowired
	public PlaceApiImpl(@Value("${google.maps.api.key}") String apiKey) {
		restTemplate = new RestTemplate();
		addPlaceApiUrl = GOOGLE_PLACES_API_ADD_PLACE_URL + "?key=" + apiKey;
		deletePlaceApiUrl = GOOGLE_PLACES_API_DELETE_PLACE_URL + "?key=" + apiKey;
	}

	public PlaceAddResponse add(PlaceAddRequest place) {
		PlaceAddResponse response = restTemplate.postForObject(this.addPlaceApiUrl, place, PlaceAddResponse.class);
		return response;
	}

	public boolean delete(String placeId) {
		boolean wasSuccessful = false;
		PlaceDeleteRequest request = new PlaceDeleteRequest();
		request.setPlaceId(placeId);
		PlaceDeleteResponse response = restTemplate.postForObject(this.deletePlaceApiUrl, request,
				PlaceDeleteResponse.class);
		if ("OK".equals(response.getStatus())) {
			wasSuccessful = true;
		}
		return wasSuccessful;
	}
}
