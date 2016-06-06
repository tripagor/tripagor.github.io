package com.tripagor.hotels;

import java.util.LinkedList;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
	public Hotel createOrModify(Hotel hotel) {
		if (hotel.getId() != null) {
			return restTemplateFactory.get(host, clientId, clientSecret).postForObject(host.concat("/hotels"), hotel,
					Hotel.class);
		} else {
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			HttpEntity<Hotel> requestEntity = new HttpEntity<>(hotel, headers);
			return restTemplateFactory.get(host, clientId, clientSecret)
					.exchange(host.concat("/hotels/{id}"), HttpMethod.PUT, requestEntity, Hotel.class, hotel.getId())
					.getBody();

		}
	}


	@Override
	public void createOrModify(Iterable<Hotel> hotels) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
		HttpEntity<Iterable<Hotel>> requestEntity = new HttpEntity<>(hotels, headers);
		restTemplateFactory.get(host, clientId, clientSecret)
				.exchange(host.concat("/hotels/"), HttpMethod.POST, requestEntity, Void.class).getBody();

	}

	@Override
	public Page<Hotel> findNewest(int currentPage, int pageSize) {
		PagedResources<Hotel> result = restTemplateFactory.get(hateoasConverter)
				.exchange(
						host.concat(
								"hotels?page={page}&size={size}&sort=bookingComId,desc"),
						HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Hotel>>() {
						}, currentPage, pageSize)
				.getBody();
		Page<Hotel> page = new PageImpl<Hotel>(new LinkedList<Hotel>(result.getContent()),
				new PageRequest(currentPage, pageSize), result.getMetadata().getTotalElements());
		return page;
	}

}
