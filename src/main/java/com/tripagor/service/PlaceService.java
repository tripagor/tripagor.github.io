package com.tripagor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.tripagor.importer.model.PlaceAddRequest;
import com.tripagor.importer.model.PlaceAddResponse;
import com.tripagor.importer.model.PlaceDeleteRequest;
import com.tripagor.importer.model.PlaceDeleteResponse;

public class PlaceService {

	private Logger logger = LoggerFactory.getLogger(PlaceService.class);
	private final static String API_KEY = "AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU";
	private final static String GOOGLE_MAPS_API_BASE_URL = "https://maps.googleapis.com/maps/api/";
	private final static String GOOGLE_PLACES_API_ADD_PLACE_URL = GOOGLE_MAPS_API_BASE_URL + "place/add/json";
	private final static String GOOGLE_PLACES_API_DELETE_PLACE_URL = GOOGLE_MAPS_API_BASE_URL + "place/delete/json";
	private final String addPlaceApiUrl;
	private RestTemplate restTemplate;
	private GeoApiContext context;
	private String deletePlaceApiUrl;

	public PlaceService() {
		context = new GeoApiContext().setApiKey(API_KEY);
		addPlaceApiUrl = GOOGLE_PLACES_API_ADD_PLACE_URL + "?key=" + API_KEY;
		deletePlaceApiUrl = GOOGLE_PLACES_API_DELETE_PLACE_URL + "?key=" + API_KEY;
		restTemplate = new RestTemplate();

	}

	public PlaceService(String apiKey) {
		context = new GeoApiContext().setApiKey(apiKey);
		addPlaceApiUrl = GOOGLE_PLACES_API_ADD_PLACE_URL + "?key=" + apiKey;
		deletePlaceApiUrl = GOOGLE_PLACES_API_DELETE_PLACE_URL + "?key=" + apiKey;
	}

	public PlacesSearchResult[] find(LatLng latLng, int radius) {
		try {
			PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, latLng).radius(radius).await();

			return response.results;
		} catch (Exception e) {
			logger.error("error with {} {} failed with {}", latLng, radius, e);
			throw new RuntimeException(e);
		}
	}

	public PlacesSearchResult[] find(String query) {
		try {
			PlacesSearchResponse response = PlacesApi.textSearchQuery(context, query).await();

			return response.results;
		} catch (Exception e) {
			logger.error("error with {} failed with {}", query);
			throw new RuntimeException(e);
		}
	}

	public PlaceAddResponse add(PlaceAddRequest place) {
		return restTemplate.postForObject(this.deletePlaceApiUrl, place, PlaceAddResponse.class);
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
