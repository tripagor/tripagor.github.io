package com.tripagor.importer;

import java.math.BigDecimal;
import java.util.Arrays;

import org.bson.Document;
import org.junit.Test;

import com.google.maps.GeoApiContext;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.tripagor.importer.model.Location;
import com.tripagor.importer.model.PlaceAddRequest;
import com.tripagor.importer.model.PlaceAddResponse;
import com.tripagor.service.PlaceAddApi;

public class PlaceIdMigration {

	@Test
	public void migrate() throws Exception {

		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU");
		PlaceAddApi placeAddApi = new PlaceAddApi("AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU");

		MongoClientURI mongoClientURI = new MongoClientURI("mongodb://localhost:27017/hotels");
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		final MongoCollection<Document> collection = mongoClient.getDatabase(mongoClientURI.getDatabase())
				.getCollection("hotel");

		FindIterable<Document> iterable = collection
				.find(new Document("well_formatted_address", new Document("$exists", true))
						.append("place_id", new Document("$exists", false)).append("is_marker_set", true)
						.append("is_marker_approved", false));

		try {

			iterable.forEach(new Block<Document>() {

				public void apply(Document document) {
					try {

						System.out.println(document.get("booking_com_id") + " " + document.get("name"));

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
