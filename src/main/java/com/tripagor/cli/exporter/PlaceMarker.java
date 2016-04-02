package com.tripagor.cli.exporter;

import java.math.BigDecimal;
import java.util.Arrays;

import org.bson.Document;
import org.springframework.web.client.RestTemplate;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.tripagor.cli.service.PlaceAddApi;
import com.tripagor.model.Location;
import com.tripagor.model.PlaceAddRequest;
import com.tripagor.model.PlaceAddResponse;
import com.tripagor.rest.RestTemplateFactory;

public class PlaceMarker {

	private int numberOfPlacesToAdd;
	private String appendStr = "";
	private RestTemplateFactory restTemplateFactory;

	public PlaceMarker() {
		this.restTemplateFactory = new RestTemplateFactory();
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
	

	public void doMark(String restUri, String clientId, String clientSecret, String key) {
		// TODO Auto-generated method stub
		
	}


	public void setNumberOfPlacesToAdd(int numberOfPlacesToAdd) {
		this.numberOfPlacesToAdd = numberOfPlacesToAdd;
	}

	public void setAppendStr(String appendStr) {
		this.appendStr = appendStr;
	}

}
