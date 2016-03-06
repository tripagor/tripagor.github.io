package com.tripagor.exporter;

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
import com.tripagor.importer.StringComparisonWeight;
import com.tripagor.importer.model.Address;
import com.tripagor.importer.model.Result;
import com.tripagor.service.AddressNormalizer;
import com.tripagor.service.PlaceService;

public class UnmarkedLodgingPlacesExporter {
	private int numberOfPlacesToAdd = 50000;
	private PlaceService placeService;
	private AddressNormalizer addressNormalizer;
	private StringComparisonWeight stringComparisonWeight;

	public UnmarkedLodgingPlacesExporter() {
		placeService = new PlaceService();
		addressNormalizer = new AddressNormalizer();
		stringComparisonWeight = new StringComparisonWeight();
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
						float weight = stringComparisonWeight.getWeightLevenshtein(document.getString("name"), result.name);

						System.out.println(document.getString("name")+"=="+result.name +"? "+weight);
						if ( weight> 0.7
								&& "APP".equals(result.scope.name())) {
							System.out.println(document.getString("name") + " WAS MARKED!");
							break;
						} else if (weight > 0.7
								&& "GOOGLE".equals(result.scope.name())) {
							System.out.println(document.getString("name")+ " APPROVED BY GOOGLE!");
							isApprovedByGoogle = true;
							break;
						}
					}
					String wellformattedAddress = null;
					if (!isApprovedByGoogle) {

						List<Result> geoCodingResults = addressNormalizer.reverseGeocoding(
								Double.parseDouble(document.getString("latitude")),
								Double.parseDouble(document.getString("longitude")));
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
						System.out.println("Setting wellformatted "+wellformattedAddress);
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
