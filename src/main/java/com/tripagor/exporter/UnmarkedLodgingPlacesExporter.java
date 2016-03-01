package com.tripagor.exporter;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripagor.importer.model.Lodging;
import com.tripagor.service.PlaceService;

import se.walkercrou.places.PlaceBuilder;

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
				PlaceBuilder builder = new PlaceBuilder(accommodation.getName(),
						accommodation.getAddress().getLatitude(), accommodation.getAddress().getLongitude(), "LODGING");
				try {
					builder.address(accommodation.getAddress().toWellFormattedString()).website(accommodation.getUrl());
					System.out.println("accomodationAddress=" + accommodation.getAddress().toWellFormattedString());
					placeService.addPlace(builder);
				} catch (Exception e) {
					// ignore it;
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
