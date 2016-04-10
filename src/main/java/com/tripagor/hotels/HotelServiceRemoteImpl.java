package com.tripagor.hotels;

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
import com.tripagor.hotels.model.Hotel;
import com.tripagor.rest.RestTemplateFactory;

public class HotelServiceRemoteImpl implements HotelService {
	private RestTemplateFactory restTemplateFactory;
	private String clientId;
	private String clientSecret;
	private String host;
	private MappingJackson2HttpMessageConverter hateoasConverter;

	public HotelServiceRemoteImpl(String host, String clientId, String clientSecret) {
		restTemplateFactory = new RestTemplateFactory();
		this.host = host;
		this.clientId = clientId;
		this.clientSecret = clientSecret;

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Jackson2HalModule());

		hateoasConverter = new MappingJackson2HttpMessageConverter();
		hateoasConverter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
		hateoasConverter.setObjectMapper(mapper);
	}

	@Override
	public Hotel getByBookingComId(Long bookingComId) {
		return restTemplateFactory.get().getForObject(
				host.concat("/hotels/search/findByBookingComId?bookingId={bookingComId}"), Hotel.class, bookingComId);
	}

	@Override
	public Hotel create(Hotel hotel) {
		return restTemplateFactory.get(host, clientId, clientSecret).postForObject(host.concat("/hotels"), hotel,
				Hotel.class);
	}

	@Override
	public Hotel update(Hotel hotel) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		HttpEntity<Hotel> requestEntity = new HttpEntity<>(hotel, headers);
		return restTemplateFactory.get(host, clientId, clientSecret)
				.exchange(host.concat("/hotels/{id}"), HttpMethod.PUT, requestEntity, Hotel.class, hotel.getId())
				.getBody();
	}

	@Override
	public PagedResources<Hotel> findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExistsAndPlaceIdExists(
			int currentPage, int pageSize, boolean isEvaluated, boolean isMarkerSet, boolean isMarkerApproved,
			boolean isFormettedAddressExisting, boolean isPlaceIdExisting) {
		return restTemplateFactory.get(hateoasConverter)
				.exchange(
						host.concat(
								"hotels/search/findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExistsAndPlaceIdExisting?page={page}&size={size}&sort=bookingComId,desc&isEvaluated={isEvaluated}&isMarkerSet={isMarkerSet}&isMarkerApproved={isMarkerSet}&isFormattedAddressExisting={isFormettedAddressExisting}&isPlaceIdExisting={isPlaceIdExisting}"),
						HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Hotel>>() {
						}, currentPage, pageSize, isEvaluated, isMarkerSet, isMarkerApproved,
						isFormettedAddressExisting, isPlaceIdExisting)
				.getBody();
	}

	@Override
	public PagedResources<Hotel> findByIsEvaluatedExists(int currentPage, Object pageSize, boolean b) {
		return restTemplateFactory.get(hateoasConverter)
				.exchange(
						host.concat(
								"hotels/search/findByIsEvaluatedExists?isEvaluatedExisting=false&page={page}&size={pageSize}&sort=bookingComId,desc"),
						HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Hotel>>() {
						}, currentPage, pageSize)
				.getBody();
	}

}
