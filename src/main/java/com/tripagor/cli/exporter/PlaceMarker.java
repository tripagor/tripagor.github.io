package com.tripagor.cli.exporter;

import java.math.BigDecimal;
import java.util.Arrays;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.tripagor.cli.service.PlaceAddApi;
import com.tripagor.model.Location;
import com.tripagor.model.PlaceAddRequest;
import com.tripagor.model.PlaceAddResponse;

public class PlaceMarker {

	private int numberOfPlacesToAdd;
	private PlaceAddApi placeAddApi;
	private static final String GOOGLE_MAPS_API_KEY = "" + "";
	private String appendStr = "";

	public PlaceMarker() {
		placeAddApi = new PlaceAddApi(GOOGLE_MAPS_API_KEY);
	}

	public void doMark(String uri, String collectionName) {

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

	public void setNumberOfPlacesToAdd(int numberOfPlacesToAdd) {
		this.numberOfPlacesToAdd = numberOfPlacesToAdd;
	}

	public void setAppendStr(String appendStr) {
		this.appendStr = appendStr;
	}

}
