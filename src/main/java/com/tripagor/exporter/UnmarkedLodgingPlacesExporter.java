package com.tripagor.exporter;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;
import com.tripagor.importer.model.Lodging;
import com.tripagor.importer.model.PlaceAddRequest;
import com.tripagor.importer.model.PlaceAddResponse;
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

				PlacesSearchResult[] results = placeService.find(
						new LatLng(accommodation.getAddress().getLatitude(), accommodation.getAddress().getLatitude()),
						100);
				boolean isApprovedByGoogle = false;
				for (PlacesSearchResult result : results) {
					if (accommodation.getName().equals(result.name) && "APP".equals(result.scope)) {
						System.out.println("deleting "+accommodation.getName());
						placeService.delete(result.placeId);
					} else if (accommodation.getName().equals(result.name) && "GOOGLE".equals(result.scope)) {
						System.out.println(accommodation.getName()+" APPROVED BY GOOGLE!");
						isApprovedByGoogle = true;
						break;
					}
				}

				if (!isApprovedByGoogle) {
					PlaceAddRequest place = new PlaceAddRequest();
					place.setName(accommodation.getName());
					place.setFormattedAddress(accommodation.getAddress().getWellFormattedAddress());
					place.setWebsite(accommodation.getUrl() + "?aid=948836");
					place.getTypes().add("lodging");
					place.getLocation().setLat(accommodation.getAddress().getLatitude());
					place.getLocation().setLng(accommodation.getAddress().getLongitude());

					try {
						PlaceAddResponse add = placeService.add(place);
						System.out.println("Added place "+place+" resulting in status="+add.getStatus());
						if (!"OK".equals(add.getStatus())) {
							System.err.println("could not add place");
						}
					} catch (Exception e) {
						System.err.println("error " + e);
					}
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
