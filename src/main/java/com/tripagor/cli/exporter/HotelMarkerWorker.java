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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.tripagor.cli.service.AddressTools;
import com.tripagor.cli.service.DistanceCalculator;
import com.tripagor.cli.service.PlaceAddApi;
import com.tripagor.cli.service.StringSimilarity;
import com.tripagor.hotels.HotelRepository;
import com.tripagor.hotels.model.Hotel;
import com.tripagor.markers.HotelMarkerRespository;
import com.tripagor.markers.model.HotelMarker;
import com.tripagor.markers.model.Location;
import com.tripagor.markers.model.Scope;
import com.tripagor.model.PlaceAddRequest;
import com.tripagor.model.PlaceAddResponse;

@Component
public class HotelMarkerWorker {
	private StringSimilarity stringSimilarity;
	private DistanceCalculator distanceCalculator;
	private AddressTools addressTools;
	private int pageSize = 50;
	private int accuracy = 30;
	private HotelRepository hotelRepository;
	private GeoApiContext geoApiContext;
	private Logger logger = LoggerFactory.getLogger(HotelMarkerWorker.class);
	private PlaceAddApi placeAddApi;
	private HotelMarkerRespository hotelMarkerRespository;

	@Autowired
	public HotelMarkerWorker(HotelRepository hotelRepository, HotelMarkerRespository hotelMarkerRespository,
			PlaceAddApi placeAddApi, GeoApiContext geoApiContext) {
		stringSimilarity = new StringSimilarity();
		distanceCalculator = new DistanceCalculator();
		addressTools = new AddressTools();

		this.hotelRepository = hotelRepository;
		this.hotelMarkerRespository = hotelMarkerRespository;
		this.placeAddApi = placeAddApi;
		this.geoApiContext = geoApiContext;
	}

	public Collection<HotelMarker> doHandle(int numberOfPlacesToMark, String appendStr) {
		Collection<HotelMarker> markers = new LinkedList<>();
		int currentNumberMarked = 0;

		int currentPage = 0;
		int totalPages = 1;

		while (currentPage < totalPages && currentNumberMarked < numberOfPlacesToMark) {
			Page<Hotel> pagedResources = hotelRepository.findAll(new Sort(Direction.DESC, "bookingComId"),
					new PageRequest(currentPage++, pageSize));
			totalPages = pagedResources.getTotalPages();
			Collection<Hotel> hotels = pagedResources.getContent();

			for (Hotel hotel : hotels) {
				if (currentNumberMarked >= numberOfPlacesToMark) {
					break;
				}

				HotelMarker hotelMarker = hotelMarkerRespository
						.findByReference(new Long(hotel.getBookingComId()).toString());
				if (hotelMarker == null) {
					hotelMarker = new HotelMarker();
					boolean isApprovedByGoogle = false;
					String placeId = null;
					String wellformattedAddress = null;
					String website = null;
					boolean isOwned = true;
					Location location = null;
					String phoneNumber = null;

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

						PlacesSearchResponse response = PlacesApi.nearbySearchQuery(geoApiContext, latLng)
								.rankby(RankBy.DISTANCE).keyword(query).await();
						for (PlacesSearchResult result : response.results) {
							float cosineDistance = stringSimilarity.cosineDistance(hotel.getName(), result.name);
							float jaroDistance = stringSimilarity.jaroDistance(hotel.getName(), result.name);
							float geometricalDistance = distanceCalculator.distance(latLng, result.geometry.location);

							if (cosineDistance >= 0.5 || jaroDistance >= 0.8 || geometricalDistance <= accuracy) {
								PlaceDetails details = PlacesApi.placeDetails(geoApiContext, result.placeId).await();
								wellformattedAddress = result.formattedAddress;
								placeId = result.placeId;
								website = details.website.toString();
								location = new Location(details.geometry.location.lat, details.geometry.location.lng);
								phoneNumber = details.internationalPhoneNumber;
								if ("APP".equals(result.scope.name())) {
									logger.debug("{} ALREADY MARKED", hotel.getName());
									isApprovedByGoogle = false;
									isOwned = true;
								} else if ("GOOGLE".equals(result.scope.name())) {
									logger.debug("{} ALREADY MARKED BY GOOGLE", hotel.getName());
									isApprovedByGoogle = true;
									isOwned = false;
								}
								break;
							}
						}

						if (address != null && !isApprovedByGoogle && placeId == null) {
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
									place.setLocation(new com.tripagor.model.Location(
											new BigDecimal(hotel.getLatitude()).doubleValue(),
											new BigDecimal(hotel.getLongitude()).doubleValue()));
									place.setAccuracy(new BigDecimal(
											distanceCalculator.distance(latLng, result.geometry.location))
													.setScale(0, RoundingMode.CEILING).intValue());
									place.setWebsite(hotel.getUrl() + appendStr);
									place.setTypes(Arrays.asList(new String[] { "lodging" }));
									place.setLanguage("en");

									PlaceAddResponse placeAddResponse = placeAddApi.add(place);

									if ("OK".equals(placeAddResponse.getStatus())) {
										placeId = placeAddResponse.getPlaceId();
										markers.add(hotelMarker);
										currentNumberMarked++;
									}
									break;
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						Scope scope = Scope.APP;
						if (isApprovedByGoogle) {
							scope = Scope.GOOGLE;
						}
						hotelMarker.setAddress(wellformattedAddress);
						hotelMarker.setIsOwned(isOwned);
						hotelMarker.setLocation(location);
						hotelMarker.setName(hotel.getName());
						hotelMarker.setPhoneNumber(phoneNumber);
						hotelMarker.setPlaceId(placeId);
						hotelMarker.setReference(new Long(hotel.getBookingComId()).toString());
						hotelMarker.setScope(scope);
						hotelMarker.setWebsite(website);
						hotelMarkerRespository.save(hotelMarker);
					}
				}
			}
		}
		return markers;
	}

}
