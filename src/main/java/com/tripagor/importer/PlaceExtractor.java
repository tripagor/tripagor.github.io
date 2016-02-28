package com.tripagor.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

public class PlaceExtractor {

	private Logger logger = LoggerFactory.getLogger(PlaceExtractor.class);
	private final static String API_KEY = "AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU";
	private GeoApiContext context;

	public PlaceExtractor() {
		context = new GeoApiContext().setApiKey(API_KEY);
	}

	public PlacesSearchResult[] findPlaces(PlaceType placeType, LatLng latLng, int radius) throws RuntimeException {
		try {
			PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, latLng).type(placeType).radius(radius)
					.await();

			return response.results;
		} catch (Exception e) {
			logger.error("error with {} {} {} failed with {}", placeType, latLng, radius, e);
			throw new RuntimeException(e);
		}
	}

	public PlacesSearchResult[] findPlaces(String query) throws RuntimeException {
		try {
			PlacesSearchResponse response = PlacesApi.textSearchQuery(context, query).await();

			return response.results;
		} catch (Exception e) {
			logger.error("error with {} failed with {}", query);
			throw new RuntimeException(e);
		}
	}

}
