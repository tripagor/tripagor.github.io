package com.tripagor.importer;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;
import com.tripagor.importer.model.Accommodation;

public class MarkerTest {
	private PlaceExtractor placeExtractor;
	private ObjectMapper objectMapper;
	private AccommodationExport accommodationExport;

	@Before
	public void before() {
		placeExtractor = new PlaceExtractor();
		objectMapper = new ObjectMapper();
		accommodationExport = new AccommodationExport();
	}

	@Test
	public void doIt() throws Exception {
		List<Accommodation> notMarked = new LinkedList<Accommodation>();
		List<Accommodation> accommodations = objectMapper.readValue(new File("src/main/resources/frankfurt.json"),
				objectMapper.getTypeFactory().constructCollectionType(List.class, Accommodation.class));
		for (Accommodation accommodation : accommodations) {
			PlacesSearchResult[] places = placeExtractor.findPlaces(
					new LatLng(accommodation.getAddress().getLatitude(), accommodation.getAddress().getLongitude()),
					30);
			boolean isMarked = false;
			for (PlacesSearchResult place : places) {

				int length = accommodation.getName().length();
				if (place.name.length() > accommodation.getName().length()) {
					length = place.name.length();
				}
				if (StringUtils.getLevenshteinDistance(place.name, accommodation.getName()) / length < 0.2) {
					isMarked = true;
				}
			}

			if (!isMarked) {
				notMarked.add(accommodation);
			}
		}
		accommodationExport.export(notMarked, new File("src/main/resources/notExportedFrankfurt.json"));

	}
}
