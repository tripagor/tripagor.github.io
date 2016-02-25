package com.tripagor.importer;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
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
			String addressLine = accommodation.getAddress().getStreet().concat(",")
					.concat(accommodation.getAddress().getCity()).concat(",")
					.concat(accommodation.getAddress().getCountry());

			GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU");
			

			GeocodingResult[] results = GeocodingApi.geocode(context, addressLine).await();
			PlacesSearchResult[] places = placeExtractor.getAddressDetails(PlaceType.LODGING, results[0].geometry.location, 20);
			System.out.println(""+places);

		}
	}
}
