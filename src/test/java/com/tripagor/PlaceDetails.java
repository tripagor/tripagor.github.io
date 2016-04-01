package com.tripagor;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
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

		@SuppressWarnings("unchecked")
		Collection<LinkedHashMap<Object, Object>> hotelmap = restTemplate.getForObject(
				"http://api.tripagor.com/hotels/search/findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExists?isEvaluated=true&isMarkerSet=true&isMarkerApproved=false&isFormattedAddressExisting=true",
				PagedResources.class).getContent();

		for (LinkedHashMap<Object, Object> map: hotelmap) {
			com.google.maps.model.PlaceDetails result = PlacesApi.placeDetails(context, map.get("placeId").toString()).await();
			System.out.println("booking_com_id=" +  map.get("bookingComId").toString() + " name=" + result.name + " location="
					+ result.geometry.location + " placeId=" + result.placeId + " scope=" + result.scope);
		}

	}
}
