package com.tripagor.exporter;

import org.bson.Document;

import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResult;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.tripagor.service.PlaceService;

public class UnmarkedLodgingPlacesExporter {
	private int numberOfPlacesToAdd = 50000;
	private PlaceService placeService;

	public UnmarkedLodgingPlacesExporter() {
		placeService = new PlaceService();
	}

	public void export(String dbUri, String collectionName) {
		MongoClientURI mongoClientURI = new MongoClientURI(dbUri);
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		final MongoCollection<Document> collection = mongoClient.getDatabase(mongoClientURI.getDatabase())
				.getCollection(collectionName);

		FindIterable<Document> iterable = collection.find(new Document("is_evaluated", false))
				.limit(numberOfPlacesToAdd);

		try {

			iterable.forEach(new Block<Document>() {

				public void apply(Document document) {
					PlacesSearchResult[] results = placeService
							.find(new LatLng(Double.parseDouble(document.getString("latitude")),
									Double.parseDouble(document.getString("longitude"))), PlaceType.LODGING);
					boolean isApprovedByGoogle = false;
					boolean isMarketSet = false;
					for (PlacesSearchResult result : results) {
						if (document.getString("name").equals(result.name) && "APP".equals(result.scope.name())) {
							System.out.println(document.getString("name") + " WAS MARKED!");
						} else if (document.getString("name").equals(result.name)
								&& "GOOGLE".equals(result.scope.name())) {
							System.out.println(document.getString("name") + " APPROVED BY GOOGLE!");
							isApprovedByGoogle = true;
						}
					}
					collection.updateOne(new Document("_id", document.get("_id")),
							new Document("$set", new Document("is_evaluated", true).append("is_marker_set", isMarketSet)
									.append("is_marker_approved", isApprovedByGoogle)));
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

}
