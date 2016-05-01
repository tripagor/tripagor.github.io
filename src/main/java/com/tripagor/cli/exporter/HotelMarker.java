package com.tripagor.cli.exporter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

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
import com.tripagor.cli.service.PlaceAddApi;
import com.tripagor.cli.service.StringSimilarity;
import com.tripagor.hotels.HotelService;
import com.tripagor.hotels.model.Hotel;
import com.tripagor.model.Location;
import com.tripagor.model.PlaceAddRequest;
import com.tripagor.model.PlaceAddResponse;

@Component
public class HotelMarker {
	private StringSimilarity stringSimilarity;
	private DistanceCalculator distanceCalculator;
	private AddressTools addressTools;
	private int pageSize = 100;
	private int accuracy = 30;
	private HotelService hotelService;
	private GeoApiContext geoApiContext;
	private Logger logger = LoggerFactory.getLogger(HotelMarker.class);
	private PlaceAddApi placeAddApi;

	@Autowired
	public HotelMarker(HotelService hotelService, PlaceAddApi placeAddApi, GeoApiContext geoApiContext) {
		stringSimilarity = new StringSimilarity();
		distanceCalculator = new DistanceCalculator();
		addressTools = new AddressTools();

		this.hotelService = hotelService;
		this.placeAddApi = placeAddApi;
		this.geoApiContext = geoApiContext;
	}

	public Collection<Hotel> doHandle(int numberOfPlacesToMark, String appendStr) {
		Collection<Hotel> markedHotels = new LinkedList<>();
		int currentNumberMarked = 0;

		int currentPage = 0;
		int totalPages = 1;

		while (currentPage < totalPages && currentNumberMarked < numberOfPlacesToMark) {
			Page<Hotel> pagedResources = hotelService.findByIsEvaluatedExists(currentPage++, pageSize, false);
			totalPages = pagedResources.getTotalPages();
			Collection<Hotel> hotels = pagedResources.getContent();

			for (Hotel hotel : hotels) {
				if (currentNumberMarked >= numberOfPlacesToMark) {
					break;
				}

				try {
					double longitude = new BigDecimal(hotel.getLongitude()).doubleValue();
					double latitude = new BigDecimal(hotel.getLatitude()).doubleValue();
					LatLng latLng = new LatLng(latitude, longitude);
					String query = hotel.getName() + ", " + hotel.getCity() + ", "
							+ new Locale("en", hotel.getCountryCode()).getDisplayCountry(new Locale("en"));
					String address = null;
					if (hotel.getAddress() != null && hotel.getCity() != null && hotel.getCountryCode() != null) {
						address = hotel.getAddress() + ", " + hotel.getCity() + ", "
								+ new Locale("en", hotel.getCountryCode()).getDisplayCountry(new Locale("en"));
					} else {
						continue;
					}

					boolean isApprovedByGoogle = false;
					boolean isMarketSet = false;
					String placeId = null;

					PlacesSearchResponse response = PlacesApi.nearbySearchQuery(geoApiContext, latLng)
							.rankby(RankBy.DISTANCE).keyword(query).await();
					for (PlacesSearchResult result : response.results) {
						float cosineDistance = stringSimilarity.cosineDistance(hotel.getName(), result.name);
						float jaroDistance = stringSimilarity.jaroDistance(hotel.getName(), result.name);
						float geometricalDistance = distanceCalculator.distance(latLng, result.geometry.location);

						if (cosineDistance >= 0.5 || jaroDistance >= 0.8 || geometricalDistance <= accuracy) {
							if ("APP".equals(result.scope.name())) {
								logger.debug("{} ALREADY MARKED", hotel.getName());
								isMarketSet = true;
								break;
							} else if ("GOOGLE".equals(result.scope.name())) {
								logger.debug("{} ALREADY MARKED BY GOOGLE", hotel.getName());
								isMarketSet = true;
								isApprovedByGoogle = true;
								break;
							}
						}
					}

					String wellformattedAddress = null;
					if (!isApprovedByGoogle && address != null) {
						GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, address)
								.resultType(AddressType.STREET_ADDRESS).await();
						for (GeocodingResult result : results) {
							if (addressTools.isProperStreetAddress(result)
									&& distanceCalculator.distance(latLng, result.geometry.location) <= accuracy) {
								wellformattedAddress = result.formattedAddress;

								logger.debug(hotel.getBookingComId() + " " + hotel.getName());

								PlaceAddRequest place = new PlaceAddRequest();

								place.setName(hotel.getName());
								place.setAddress(wellformattedAddress);
								Location location = new Location();
								location.setLat(new BigDecimal(hotel.getLatitude()).doubleValue());
								location.setLng(new BigDecimal(hotel.getLongitude()).doubleValue());
								place.setLocation(location);
								place.setAccuracy(
										new BigDecimal(distanceCalculator.distance(latLng, result.geometry.location))
												.setScale(1, RoundingMode.CEILING).intValue());
								place.setWebsite(hotel.getUrl() + appendStr);
								place.setTypes(Arrays.asList(new String[] { "lodging" }));
								place.setLanguage("en");

								PlaceAddResponse placeAddResponse = placeAddApi.add(place);
								// if
								// ("OK".equals(placeAddResponse.getStatus())) {
								// isMarketSet = true;
								// placeId = placeAddResponse.getPlaceId();
								// markedHotels.add(hotel);
								// currentNumberMarked++;
								// }

								currentNumberMarked++;
								break;
							}
						}
					}

					hotel.setIsEvaluated(true);
					hotel.setIsMarkerSet(isMarketSet);
					hotel.setIsMarkerApproved(isApprovedByGoogle);
					hotel.setFormattedAddress(wellformattedAddress);
					hotel.setPlaceId(placeId);

					// hotelService.update(hotel);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return markedHotels;
	}

}
