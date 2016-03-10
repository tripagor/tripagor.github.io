package com.tripagor.importer;

import java.math.BigDecimal;
import java.util.Locale;

import org.bson.Document;
import org.junit.Test;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

public class PlaceIdMigration {

	@Test
	public void migrate() throws Exception {

		final GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU");

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
						double longitude = new BigDecimal(document.getString("longitude")).doubleValue();
						double latitude = new BigDecimal(document.getString("latitude")).doubleValue();
						LatLng latLng = new LatLng(latitude, longitude);
						String query = document.getString("name") + ", " + document.getString("city_hotel") + ", "
								+ new Locale("", document.getString("country_code")).getDisplayCountry();
						System.out.println(document.get("booking_com_id") + " " + document.get("name"));
						PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, latLng)
								.rankby(RankBy.DISTANCE).keyword(query).await();
						String placeId = null;
						for (PlacesSearchResult result : response.results) {
							if ("APP".equals(result.scope.name())) {
								placeId = result.placeId;
								break;
							}
						}
						if (placeId != null) {
							Document updateDocument = new Document("is_marker_set", true).append("place_id", placeId);
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

}
