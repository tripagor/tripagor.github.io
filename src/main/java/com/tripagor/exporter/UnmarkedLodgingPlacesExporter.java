package com.tripagor.exporter;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripagor.importer.model.Lodging;
import com.tripagor.importer.model.Place;
import com.tripagor.service.PlaceService;

public class UnmarkedLodgingPlacesExporter {
	private PlaceService placeService;
	private ObjectMapper objectMapper;
	private int numberOfPlacesToAdd = 50000;

	public UnmarkedLodgingPlacesExporter() {
		objectMapper = new ObjectMapper();
	}

	public void export(File importFile) {
		placeService = new PlaceService();
		try {
			List<Lodging> accommodations = objectMapper.readValue(importFile, new TypeReference<List<Lodging>>() {
			});
			int numOfAdds = 0;
			for (Lodging accommodation : accommodations) {
				if (numOfAdds >= numberOfPlacesToAdd) {
					break;
				}
				numOfAdds++;
				Place place = new Place();
				place.setName(accommodation.getName());
				place.setFormattedAddress(accommodation.getAddress().toWellFormattedString());
				place.setWebsite(accommodation.getUrl() + "?aid=948836");
				place.getTypes().add("lodging");
				place.getLocation().setLat(accommodation.getAddress().getLatitude());
				place.getLocation().setLng(accommodation.getAddress().getLongitude());

				try {
					placeService.addPlace(place);
				} catch (Exception e) {
					System.err.println("error " + e);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("failed", e);
		}

	}

	public void setNumberOfPlacesToAdd(int numberOfPlacesToAdd) {
		this.numberOfPlacesToAdd = numberOfPlacesToAdd;
	}

}
