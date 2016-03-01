package com.tripagor.service;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.tripagor.importer.model.Place;

public class PlaceService {

	private Logger logger = LoggerFactory.getLogger(PlaceService.class);
	private final static String API_KEY = "AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU";
	private final static String GOOGLE_PLACES_API_ADD_PLACE_URL = "https://maps.googleapis.com/maps/api/place/add/json";
	private final String addPlaceApiUrl;
	private GeoApiContext context;

	public PlaceService() {
		context = new GeoApiContext().setApiKey(API_KEY);
		addPlaceApiUrl = GOOGLE_PLACES_API_ADD_PLACE_URL + "?key=" + API_KEY;

	}

	public PlaceService(String apiKey) {
		context = new GeoApiContext().setApiKey(apiKey);
		addPlaceApiUrl = GOOGLE_PLACES_API_ADD_PLACE_URL + "?key=" + apiKey;
	}

	public PlacesSearchResult[] findPlaces(LatLng latLng, int radius) {
		try {
			PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, latLng).radius(radius).await();

			return response.results;
		} catch (Exception e) {
			logger.error("error with {} {} failed with {}", latLng, radius, e);
			throw new RuntimeException(e);
		}
	}

	public PlacesSearchResult[] findPlaces(String query) {
		try {
			PlacesSearchResponse response = PlacesApi.textSearchQuery(context, query).await();

			return response.results;
		} catch (Exception e) {
			logger.error("error with {} failed with {}", query);
			throw new RuntimeException(e);
		}
	}

	public String addPlace(Place place) {

		Client client = Client.create();

		WebResource webResource = client.resource(this.addPlaceApiUrl);
		try {
			ClientResponse response = webResource.type(MediaType.APPLICATION_JSON_TYPE)
					.accept(MediaType.APPLICATION_JSON_TYPE)
					.post(ClientResponse.class, new ObjectMapper().writeValueAsString(place));

			if (response.getStatus() != 200) {
				System.err.println("failed "+response.getStatus());
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			System.out.println(""+response.getProperties().get("place_id"));
			return (String) response.getProperties().get("place_id");
		} catch (Exception e) {
			System.err.println("error "+e);
			throw new RuntimeException(e);
		}
	}
}
