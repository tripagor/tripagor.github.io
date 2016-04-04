package com.tripagor.cli.exporter;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Locale;

import org.springframework.hateoas.PagedResources;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.tripagor.cli.service.AddressTools;
import com.tripagor.cli.service.DistanceCalculator;
import com.tripagor.cli.service.StringSimilarity;
import com.tripagor.hotels.HotelService;
import com.tripagor.hotels.model.Hotel;

public class UnmarkedLodgingPlacesFinder {
	private int numberOfPlacesToAdd = 50000;
	private StringSimilarity stringSimilarity;
	private DistanceCalculator distanceCalculator;
	private AddressTools addressTools;
	private Object pageSize = 50;
	private HotelService hotelService;

	public UnmarkedLodgingPlacesFinder(HotelService hotelService) {
		stringSimilarity = new StringSimilarity();
		distanceCalculator = new DistanceCalculator();
		addressTools = new AddressTools();

		this.hotelService = hotelService;
	}

	public void setNumberOfPlacesToAdd(int numberOfPlacesToAdd) {
		this.numberOfPlacesToAdd = numberOfPlacesToAdd;
	}

	public void export(String host, String clientId, String clientSecret, String key) {
		GeoApiContext context = new GeoApiContext().setApiKey(key);

		boolean isMaxiumimNumber = false;
		int currentNumberProcessed = 0;

		int currentPage = 0;
		long totalPages = 1;

		while (currentPage < totalPages && !isMaxiumimNumber) {
			PagedResources<Hotel> pagedResources = hotelService.findByIsEvaluatedExists(currentPage++, pageSize, true);
			totalPages = pagedResources.getMetadata().getTotalPages();
			Collection<Hotel> hotels = pagedResources.getContent();

			for (Hotel hotel : hotels) {

				if (numberOfPlacesToAdd != 0 && currentNumberProcessed >= numberOfPlacesToAdd) {
					isMaxiumimNumber = true;
					break;
				}

				try {
					double longitude = new BigDecimal(hotel.getLongitude()).doubleValue();
					double latitude = new BigDecimal(hotel.getLatitude()).doubleValue();
					LatLng latLng = new LatLng(latitude, longitude);
					String query = hotel.getName() + ", " + hotel.getCity() + ", "
							+ new Locale("", hotel.getCountryCode()).getDisplayCountry();
					String address = null;
					if (hotel.getAddress() != null && hotel.getCity() != null && hotel.getCountryCode() != null) {
						address = hotel.getAddress() + ", " + hotel.getCity() + ", "
								+ new Locale("", hotel.getCountryCode()).getDisplayCountry();
					} else {
						continue;
					}
					PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, latLng).rankby(RankBy.DISTANCE)
							.keyword(query).await();
					boolean isApprovedByGoogle = false;
					boolean isMarketSet = false;
					for (PlacesSearchResult result : response.results) {
						float cosineDistance = stringSimilarity.cosineDistance(hotel.getName(), result.name);
						float jaroDistance = stringSimilarity.jaroDistance(hotel.getName(), result.name);
						float geometricalDistance = distanceCalculator.distance(latLng, result.geometry.location);

						if (cosineDistance >= 0.5 || jaroDistance >= 0.8 || geometricalDistance <= 50) {
							if ("APP".equals(result.scope.name())) {
								isMarketSet = true;
								System.out.println(hotel.getName() + " WAS MARKED!");
								break;
							} else if ("GOOGLE".equals(result.scope.name())) {
								isMarketSet = true;
								isApprovedByGoogle = true;
								System.out.println(hotel.getName() + " IS MARKED AND APPROVED BY GOOGLE!");
								break;
							}

						}
					}
					String wellformattedAddress = null;
					if (!isApprovedByGoogle && address != null) {
						GeocodingResult[] results = GeocodingApi.geocode(context, address)
								.resultType(AddressType.STREET_ADDRESS).await();
						for (GeocodingResult result : results) {
							if (addressTools.isProperStreetAddress(result)) {
								wellformattedAddress = result.formattedAddress;
								break;
							}
						}
					}

					hotel.setIsEvaluated(true);
					hotel.setIsMarkerSet(isMarketSet);
					hotel.setIsMarkerApproved(isApprovedByGoogle);
					if (wellformattedAddress != null) {
						System.out.println("Setting wellformatted " + wellformattedAddress);
						hotel.setFormattedAddress(wellformattedAddress);
					}

					hotelService.update(hotel);
					currentNumberProcessed++;
				} catch (Exception e) {
				}
			}
		}

	}

}
