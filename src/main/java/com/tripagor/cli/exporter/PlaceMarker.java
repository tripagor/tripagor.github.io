package com.tripagor.cli.exporter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

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
import com.tripagor.cli.service.PlaceAddApi;
import com.tripagor.hotels.model.Hotel;
import com.tripagor.model.Location;
import com.tripagor.model.PlaceAddRequest;
import com.tripagor.model.PlaceAddResponse;
import com.tripagor.rest.RestTemplateFactory;

public class PlaceMarker {

	private int numberOfPlacesToAdd;
	private String appendStr = "";
	private int pageSize = 50;
	private RestTemplateFactory restTemplateFactory;
	private MappingJackson2HttpMessageConverter converter;

	public PlaceMarker() {
		restTemplateFactory = new RestTemplateFactory();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Jackson2HalModule());

		converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
		converter.setObjectMapper(mapper);

	}

	public void doMark(String host, String clientId, String clientSecret, String key) {
		PlaceAddApi placeAddApi = new PlaceAddApi(key);
		boolean isMaxiumimNumber = false;
		int currentNumberAdded = 0;

		int currentPage = 0;
		long totalPages = 1;

		while (currentPage < totalPages && !isMaxiumimNumber) {
			PagedResources<Hotel> pagedResources = restTemplateFactory.get(converter)
					.exchange(
							host.concat(
									"hotels/search/findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExists?page={page}&size={size}&isEvaluated=true&isMarkerSet=false&isMarkerApproved=false&isFormattedAddressExisting=true"),
							HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Hotel>>() {
							}, currentPage++, pageSize)
					.getBody();
			totalPages = pagedResources.getMetadata().getTotalPages();
			Collection<Hotel> hotels = pagedResources.getContent();

			for (Hotel hotel : hotels) {
				if (numberOfPlacesToAdd != 0 && currentNumberAdded >= numberOfPlacesToAdd) {
					isMaxiumimNumber = true;
					break;
				}
				try {
					System.out.println(hotel.getBookingComId() + " " + hotel.getName());
					PlaceAddRequest place = new PlaceAddRequest();

					place.setName(hotel.getName());
					place.setAddress(hotel.getFormattedAddress());
					Location location = new Location();
					location.setLat(new BigDecimal(hotel.getLatitude()).doubleValue());
					location.setLng(new BigDecimal(hotel.getLongitude()).doubleValue());
					place.setLocation(location);
					place.setAccuracy(30);
					place.setWebsite(hotel.getUrl() + appendStr);
					place.setTypes(Arrays.asList(new String[] { "lodging" }));

					PlaceAddResponse response = placeAddApi.add(place);
					if ("OK".equals(response.getStatus())) {
						hotel.setPlaceId(response.getPlaceId());
						hotel.setIsMarkerSet(true);

						HttpHeaders headers = new HttpHeaders();
						headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
						HttpEntity<Hotel> requestEntity = new HttpEntity<>(hotel, headers);
						restTemplateFactory.get(host, clientId, clientSecret).exchange(host.concat("/hotels/{id}"),
								HttpMethod.PATCH, requestEntity, String.class, hotel.getId());
						currentNumberAdded++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setNumberOfPlacesToAdd(int numberOfPlacesToAdd) {
		this.numberOfPlacesToAdd = numberOfPlacesToAdd;
	}

	public void setAppendStr(String appendStr) {
		this.appendStr = appendStr;
	}

}
