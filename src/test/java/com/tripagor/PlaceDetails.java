package com.tripagor;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.tripagor.hotels.model.Hotel;
import com.tripagor.rest.RestTemplateFactory;

public class PlaceDetails {

	private RestTemplate restTemplate;
	private GeoApiContext context;

	@Before
	public void before() {
		RestTemplateFactory restTemplateFactory = new RestTemplateFactory();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Jackson2HalModule());

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
		converter.setObjectMapper(mapper);

		restTemplate = restTemplateFactory.get(converter);

		context = new GeoApiContext().setApiKey("AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU");

	}

	@Test
	public void migrate() throws Exception {
		Collection<Hotel> hotels = restTemplate.exchange(
				"http://api.tripagor.com/hotels/search/findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExists?page={page}&size={size}&isEvaluated=true&isMarkerSet=true&isMarkerApproved=false&isFormattedAddressExisting=true",
				HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Hotel>>() {
				}, 0, 100).getBody().getContent();
		for (Hotel hotel : hotels) {
			com.google.maps.model.PlaceDetails result = PlacesApi.placeDetails(context, hotel.getPlaceId().toString())
					.await();
			System.out.println("name=" + result.name + " placeId=" + result.placeId + " scope=" + result.scope);
		}
	}
}
