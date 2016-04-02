package com.tripagor.cli.exporter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import org.bson.Document;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.tripagor.cli.service.PlaceAddApi;
import com.tripagor.hotels.model.Hotel;
import com.tripagor.model.Location;
import com.tripagor.model.PlaceAddRequest;
import com.tripagor.model.PlaceAddResponse;
import com.tripagor.rest.RestTemplateFactory;

public class PlaceMarker {

	private int numberOfPlacesToAdd;
	private String appendStr = "";
	private RestTemplate hateoasRestTemplate;
	private int pageSize = 40;
	private RestTemplate patchRestTemplate;
	private RestTemplateFactory restTemplateFactory;

	public PlaceMarker() {
		 restTemplateFactory = new RestTemplateFactory();

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.registerModule(new Jackson2HalModule());

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
		converter.setObjectMapper(mapper);

		hateoasRestTemplate = restTemplateFactory.get(converter);
	}

	public void doMark(String uri, String collectionName, String key) {
		PlaceAddApi placeAddApi = new PlaceAddApi(key);

		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		final MongoCollection<Document> collection = mongoClient.getDatabase(mongoClientURI.getDatabase())
				.getCollection(collectionName);

		FindIterable<Document> iterable = collection.find(
				new Document("well_formatted_address", new Document("$exists", true)).append("is_marker_set", false))
				.limit(numberOfPlacesToAdd).sort(new Document("booking_com_id", -1));

		try {

			iterable.forEach(new Block<Document>() {

				public void apply(Document document) {
					try {
						System.out.println(document.get("booking_com_id") + " " + document.get("name"));
						PlaceAddRequest place = new PlaceAddRequest();

						place.setName(document.getString("name"));
						place.setAddress(document.getString("well_formatted_address"));
						Location location = new Location();
						location.setLat(new BigDecimal(document.getString("latitude")).doubleValue());
						location.setLng(new BigDecimal(document.getString("longitude")).doubleValue());
						place.setLocation(location);
						place.setAccuracy(30);
						place.setWebsite(document.getString("hotel_url") + appendStr);
						place.setTypes(Arrays.asList(new String[] { "lodging" }));

						PlaceAddResponse response = placeAddApi.add(place);
						if ("OK".equals(response.getStatus())) {
							Document updateDocument = new Document("is_marker_set", true).append("place_id",
									response.getPlaceId());
							collection.updateOne(new Document("_id", document.get("_id")),
									new Document("$set", updateDocument));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			throw new RuntimeException("failed " + e, e);
		} finally {
			mongoClient.close();
		}

	}

	public void doMark(String host, String clientId, String clientSecret, String key) {
		PlaceAddApi placeAddApi = new PlaceAddApi(key);

		patchRestTemplate = restTemplateFactory.get(host,clientId, clientSecret);
		int currentPage = 0;
		long totalPages = 1;

		
		while (currentPage < totalPages) {
			PagedResources<Hotel> pagedResources = hateoasRestTemplate.exchange(
					host.concat("hotels/search/findByIsEvaluatedAndIsMarkerSetAndIsMarkerApprovedAndFormattedAddressExists?page={page}&size={size}&isEvaluated=true&isMarkerSet=false&isMarkerApproved=false&isFormattedAddressExisting=true"),
					HttpMethod.GET, null, new ParameterizedTypeReference<PagedResources<Hotel>>() {
					}, currentPage++, pageSize).getBody();
			totalPages = pagedResources.getMetadata().getTotalPages();
			Collection<Hotel> hotels = pagedResources.getContent();

			for (Hotel hotel : hotels) {
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

					PlaceAddResponse response = new PlaceAddResponse();// placeAddApi.add(place);
					response.setStatus("OK");
					response.setPlaceId("AAA99292");
					if ("OK".equals(response.getStatus())) {
						hotel.setPlaceId(response.getPlaceId());
						hotel.setIsMarkerSet(true);

						HttpHeaders headers = new HttpHeaders();
						headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
						HttpEntity<Hotel> requestEntity = new HttpEntity<>(hotel, headers);
						patchRestTemplate.exchange(host.concat("/hotels/{id}"), HttpMethod.PATCH, requestEntity,
								String.class, hotel.getId());
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
