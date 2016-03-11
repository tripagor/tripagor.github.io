package com.tripagor;

import org.bson.Document;
import org.junit.Test;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class PlaceDetails {
	@Test
	public void migrate() throws Exception {

		final GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU");

		MongoClientURI mongoClientURI = new MongoClientURI("mongodb://localhost:27017/hotels");
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		final MongoCollection<Document> collection = mongoClient.getDatabase(mongoClientURI.getDatabase())
				.getCollection("hotel");

		FindIterable<Document> iterable = collection.find(new Document("place_id", new Document("$exists", true)));

		try {

			iterable.forEach(new Block<Document>() {

				public void apply(Document document) {
					try {

						com.google.maps.model.PlaceDetails result = PlacesApi
								.placeDetails(context, document.getString("place_id")).await();
						System.out.println("booking_com_id=" + document.get("booking_com_id") + " name=" + result.name
								+ " location=" + result.geometry.location + " placeId" + result.placeId + " scope="
								+ result.scope);

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
}
