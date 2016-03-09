package com.tripagor.exporter;

import java.math.BigDecimal;
import java.util.Locale;

import org.bson.Document;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.PlacesApi;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.tripagor.service.AddressTools;
import com.tripagor.service.DistanceCalculator;
import com.tripagor.service.StringSimilarity;

public class UnmarkedLodgingPlacesExporter {
	private int numberOfPlacesToAdd = 50000;
	private StringSimilarity stringSimilarity;
	private DistanceCalculator distanceCalculator;
	private final GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU");
	private AddressTools addressTools;

	public UnmarkedLodgingPlacesExporter() {
		stringSimilarity = new StringSimilarity();
		distanceCalculator = new DistanceCalculator();
		addressTools = new AddressTools();
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
					try {
						double longitude = new BigDecimal(document.getString("longitude")).doubleValue();
						double latitude = new BigDecimal(document.getString("latitude")).doubleValue();
						LatLng latLng = new LatLng(latitude, longitude);
						String query = document.getString("name") + ", " + document.getString("city_hotel") + ", "
								+ new Locale("", document.getString("country_code")).getDisplayCountry();
						String address = null;
						if (document.getString("address") != null && document.getString("city_hotel") != null
								&& document.getString("country_code") != null) {
							address = document.getString("address") + ", " + document.getString("city_hotel") + ", "
									+ new Locale("", document.getString("country_code")).getDisplayCountry();
						}
						PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, latLng)
								.rankby(RankBy.DISTANCE).keyword(query).await();
						boolean isApprovedByGoogle = false;
						boolean isMarketSet = false;
						for (PlacesSearchResult result : response.results) {
							float cosineDistance = stringSimilarity.cosineDistance(document.getString("name"),
									result.name);
							float jaroDistance = stringSimilarity.jaroDistance(document.getString("name"), result.name);
							float geometricalDistance = distanceCalculator.distance(latLng, result.geometry.location);

							if (cosineDistance >= 0.5 || jaroDistance >= 0.8 || geometricalDistance <= 50) {
								if ("APP".equals(result.scope.name())) {
									isMarketSet = true;
									System.out.println(document.getString("name") + " WAS MARKED!");
									break;
								} else if ("GOOGLE".equals(result.scope.name())) {
									isMarketSet = true;
									isApprovedByGoogle = true;
									System.out
											.println(document.getString("name") + " IS MARKED AND APPROVED BY GOOGLE!");
									break;
								}

							}
						}
						String wellformattedAddress = null;
						if (!isApprovedByGoogle && address != null) {
							GeocodingResult[] results = GeocodingApi.geocode(context, address)
									.resultType(AddressType.STREET_ADDRESS).await();
							for (GeocodingResult result : results) {
								if (addressTools.isProperStreetAddress(result)) {
									wellformattedAddress = result.formattedAddress;
									break;
								}
							}

						}
						Document updateDocument = new Document("is_evaluated", true)
								.append("is_marker_set", isMarketSet).append("is_marker_approved", isApprovedByGoogle);
						if (wellformattedAddress != null) {
							System.out.println("Setting wellformatted " + wellformattedAddress);
							updateDocument.append("well_formatted_address", wellformattedAddress);
						}
						collection.updateOne(new Document("_id", document.get("_id")),
								new Document("$set", updateDocument));
					} catch (Exception e) {
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

}
