package com.tripagor.cli.exporter;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Locale;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.tripagor.hotels.model.Hotel;
import com.tripagor.rest.RestTemplateFactory;

public class UnmarkedLodgingPlacesFinder {
	private int numberOfPlacesToAdd = 50000;
	private StringSimilarity stringSimilarity;
	private DistanceCalculator distanceCalculator;
	private AddressTools addressTools;
	private RestTemplateFactory restTemplateFactory;
	private MappingJackson2HttpMessageConverter converter;
	private Object pageSize = 40;

	public UnmarkedLodgingPlacesFinder() {
		stringSimilarity = new StringSimilarity();
		distanceCalculator = new DistanceCalculator();
		addressTools = new AddressTools();

		restTemplateFactory = new RestTemplateFactory();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Jackson2HalModule());

		converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
		converter.setObjectMapper(mapper);

	}

	public void setNumberOfPlacesToAdd(int numberOfPlacesToAdd) {
		this.numberOfPlacesToAdd = numberOfPlacesToAdd;
	}

	public void export(String host, String clientId, String clientSecret, String key) {
		GeoApiContext context = new GeoApiContext().setApiKey(key);

		int currentPage = 0;
		long totalPages = 1;

		while (currentPage < totalPages) {
			PagedResources<Hotel> pagedResources = restTemplateFactory.get(converter)
					.exchange(
							host.concat(
									"hotels/search/findByIsEvaluatedExists?isEvaluatedExisting=false&page={page}&size={pageSize}&sort=bookingComId,desc"),
							HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Hotel>>() {
							}, currentPage++, pageSize)
					.getBody();
			totalPages = pagedResources.getMetadata().getTotalPages();
			Collection<Hotel> hotels = pagedResources.getContent();

			for (Hotel hotel : hotels) {
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

					HttpHeaders headers = new HttpHeaders();
					headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
					HttpEntity<Hotel> requestEntity = new HttpEntity<>(hotel, headers);
					restTemplateFactory.get(host, clientId, clientSecret).exchange(host.concat("/hotels/{id}"),
							HttpMethod.PATCH, requestEntity, String.class, hotel.getId());
				} catch (Exception e) {
				}

			}
		}

	}

}
