package com.tripagor.importer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;
import se.walkercrou.places.PlaceBuilder;

public class PlaceService {

	private Logger logger = LoggerFactory.getLogger(PlaceService.class);
	private final static String API_KEY = "AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU";
	private GeoApiContext context;
	private GooglePlaces googlePlaces;

	public PlaceService() {
		context = new GeoApiContext().setApiKey(API_KEY);
		googlePlaces = new GooglePlaces(API_KEY);
	}

	public PlaceService(String apiKey) {
		context = new GeoApiContext().setApiKey(apiKey);
		googlePlaces = new GooglePlaces(apiKey);
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

	public Place addPlace(Place place) {
		PlaceBuilder builder = new PlaceBuilder(place.getName(), place.getLatitude(), place.getLongitude(),
				place.getTypes());
		builder.address(place.getAddress()).website(place.getWebsite());
		
		return googlePlaces.addPlace(builder, true);
	}

}
