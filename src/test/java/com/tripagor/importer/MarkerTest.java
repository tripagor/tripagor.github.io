package com.tripagor.importer;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResult;
import com.tripagor.importer.model.Accommodation;

public class MarkerTest {
	private PlaceExtractor placeExtractor;
	private ObjectMapper objectMapper;

	@Before
	public void before() {
		placeExtractor = new PlaceExtractor();
		objectMapper = new ObjectMapper();
	}

	@Test
	public void doIt() throws Exception {
		List<Accommodation> accommodations = objectMapper.readValue(new File("src/test/resources/selection.json"),
				objectMapper.getTypeFactory().constructCollectionType(List.class, Accommodation.class));
		for (Accommodation accommodation : accommodations) {
			PlacesSearchResult[] places = placeExtractor.getAddressDetails(PlaceType.LODGING,
					new LatLng(accommodation.getAddress().getLatitude(), accommodation.getAddress().getLongitude()),
					30);
			boolean isMarked = false;
			for (PlacesSearchResult place : places) {

				System.out.println("accommodation.getName()=" + accommodation.getName() + " place.name " + place.name
						+ " distance=" + StringUtils.getLevenshteinDistance(place.name, accommodation.getName()));
				if (StringUtils.getLevenshteinDistance(place.name, accommodation.getName()) > 0.5) {
					isMarked = true;
				}
			}
			System.out.println(accommodation.getName() + " marked? " + isMarked);
		}
	}
}
