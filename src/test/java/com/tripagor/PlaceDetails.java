package com.tripagor;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.tripagor.hotels.model.Hotel;

public class PlaceDetails {

	RestTemplate restTemplate;

	@Before
	public void before() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Jackson2HalModule());

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
		converter.setObjectMapper(mapper);
		restTemplate = new RestTemplate(Collections.<HttpMessageConverter<?>> singletonList(converter));
	}

	@Test
	public void migrate() throws Exception {

		final GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU");

		Collection<Hotel> hotels = restTemplate.exchange(
				"http://api.tripagor.com/hotels/search/findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExists?isEvaluated=true&isMarkerSet=true&isMarkerApproved=false&isFormattedAddressExisting=true",
				HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Hotel>>() {
				}).getBody().getContent();
		for (Hotel hotel : hotels) {
			com.google.maps.model.PlaceDetails result = PlacesApi.placeDetails(context, hotel.getPlaceId().toString())
					.await();
			System.out.println("booking_com_id=" + hotel.getBookingComId() + " name=" + result.name + " location="
					+ result.geometry.location + " placeId=" + result.placeId + " scope=" + result.scope
					+ " wellformattedAddress=" + result.formattedAddress);

		}

	}
}
