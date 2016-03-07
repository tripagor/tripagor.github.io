package com.tripagor.exporter;

import java.math.BigDecimal;
import java.util.List;

import org.bson.Document;

import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResult;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.tripagor.importer.StringSimilarity;
import com.tripagor.importer.model.Address;
import com.tripagor.importer.model.Result;
import com.tripagor.service.AddressNormalizer;
import com.tripagor.service.PlaceService;

public class UnmarkedLodgingPlacesExporter {
	private int numberOfPlacesToAdd = 50000;
	private PlaceService placeService;
	private AddressNormalizer addressNormalizer;
	private StringSimilarity stringSimilarity;

	public UnmarkedLodgingPlacesExporter() {
		placeService = new PlaceService();
		addressNormalizer = new AddressNormalizer();
		stringSimilarity = new StringSimilarity();
	}

	public void export(String dbUri, String collectionName) {
		MongoClientURI mongoClientURI = new MongoClientURI(dbUri);
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		final MongoCollection<Document> collection = mongoClient.getDatabase(mongoClientURI.getDatabase())
				.getCollection(collectionName);

		FindIterable<Document> iterable = collection.find(new Document("is_evaluated", new Document("$exists", false)))
				.limit(numberOfPlacesToAdd).sort(new Document("booking_com_id", -1));

		try {

			iterable.forEach(new Block<Document>() {

				public void apply(Document document) {
					double longitude = new BigDecimal(document.getString("longitude")).doubleValue();
					double latitude = new BigDecimal(document.getString("latitude")).doubleValue();
					PlacesSearchResult[] results = placeService
							.find(document.getString("name") + ", " + document.getString("city_hotel"));
					boolean isApprovedByGoogle = false;
					boolean isMarketSet = false;
					for (PlacesSearchResult result : results) {
						float distance = stringSimilarity.cosineDistance(document.getString("name"), result.name);

						System.out.println(document.getString("name") + "==" + result.name + "? " + distance);
						if (distance > 0.5) {
							if ("APP".equals(result.scope.name())) {
								isMarketSet = true;
								System.out.println(document.getString("name") + " WAS MARKED!");
								break;
							} else if ("GOOGLE".equals(result.scope.name())) {
								isApprovedByGoogle = true;
								System.out.println(document.getString("name") + " WAS MARKED!");
								break;
							}
						}
					}
					String wellformattedAddress = null;
					if (!isApprovedByGoogle) {
						List<Result> geoCodingResults = addressNormalizer.reverseGeocoding(latitude, longitude,
								new String[] { "street_address" }, new String[] { "ROOFTOP" });
						for (Result result : geoCodingResults) {
							Address address = addressNormalizer.getAdress(result.getAddressComponents());
							if (address.getCity() != null && address.getCountry() != null
									&& address.getStreetName() != null && address.getStreetNumber() != null
									&& address.getPostalCode() != null) {
								wellformattedAddress = result.getFormattedAddress();
								break;
							}
						}

					}
					Document updateDocument = new Document("is_evaluated", true).append("is_marker_set", isMarketSet)
							.append("is_marker_approved", isApprovedByGoogle);
					if (wellformattedAddress != null) {
						System.out.println("Setting wellformatted " + wellformattedAddress);
						updateDocument.append("well_formatted_address", wellformattedAddress);
					}
					collection.updateOne(new Document("_id", document.get("_id")),
							new Document("$set", updateDocument));
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
