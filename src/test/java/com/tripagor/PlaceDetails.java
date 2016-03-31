package com.tripagor;

import java.util.List;

import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.tripagor.hotels.model.Hotel;

public class PlaceDetails {
	@Test
	public void migrate() throws Exception {

		final GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU");
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(100000);
		clientHttpRequestFactory.setReadTimeout(100000);
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
		List<Hotel> hotels = restTemplate

				.exchange(
						"http://api.tripagor.com/hotels/search/findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExists?isEvaluated=true&isMarkerSet=true&isMarkerApproved=false&isFormattedAddressExisting=true",
						HttpMethod.GET, null, new ParameterizedTypeReference<List<Hotel>>() {
						})
				.getBody();

		for (Hotel hotel : hotels) {

			com.google.maps.model.PlaceDetails result = PlacesApi.placeDetails(context, hotel.getPlaceId()).await();
			System.out.println("booking_com_id=" + hotel.getBookingComId() + " name=" + result.name + " location="
					+ result.geometry.location + " placeId=" + result.placeId + " scope=" + result.scope);
		}

	}
}
