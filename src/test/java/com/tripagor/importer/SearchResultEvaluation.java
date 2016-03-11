package com.tripagor.importer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.RankBy;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.tripagor.cli.service.DistanceCalculator;
import com.tripagor.cli.service.StringSimilarity;

public class SearchResultEvaluation {

	private final static String API_KEY = "AIzaSyC_V_8PAujfCgCSU0UOAsWJzvoIbNFKYGU";
	private GeoApiContext context;
	private DistanceCalculator distanceCalculator;

	@Before
	public void before() {
		context = new GeoApiContext().setApiKey(API_KEY);
		distanceCalculator = new DistanceCalculator();
	}

	@Test
	public void doEvaluate() {
		MongoClientURI mongoClientURI = new MongoClientURI("mongodb://localhost:27017/hotels");
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		final MongoCollection<Document> hotels = mongoClient.getDatabase(mongoClientURI.getDatabase())
				.getCollection("hotel");
		final MongoCollection<Document> searchData = mongoClient.getDatabase(mongoClientURI.getDatabase())
				.getCollection("searchdata");

		//
		FindIterable<Document> iterable = hotels.find(new Document("is_evaluated", new Document("$exists", false)))
				.limit(25).sort(new Document("booking_com_id", -1));

		try {

			iterable.forEach(new Block<Document>() {

				public void apply(Document document) {
					try {
						String query = document.getString("name") + ", " + document.getString("city_hotel") + ", "
								+ new Locale("en", document.getString("country_code")).getDisplayCountry();
						LatLng latLng = new LatLng(new BigDecimal(document.getString("latitude")).doubleValue(),
								new BigDecimal(document.getString("longitude")).doubleValue());
						List<Document> searches = new ArrayList<Document>();

						PlacesSearchResponse response = PlacesApi.nearbySearchQuery(context, latLng).radius(10000)
								.await();
						searches.add(getDocumentForSearchResult(document.getString("name"), latLng,
								"nearbySearch( latLng=" + latLng + " radius=" + 5000, response));

						response = PlacesApi.nearbySearchQuery(context, latLng).rankby(RankBy.DISTANCE)
								.type(PlaceType.LODGING).await();
						searches.add(getDocumentForSearchResult(document.getString("name"), latLng,
								"nearbySearch( latLng=" + latLng + " rankby=distance placetype=lodging", response));

						response = PlacesApi.nearbySearchQuery(context, latLng).rankby(RankBy.DISTANCE).keyword(query)
								.await();
						searches.add(getDocumentForSearchResult(document.getString("name"), latLng,
								"nearbySearch( latLng=" + latLng + " rankby=distance keyword=" + query, response));

						response = PlacesApi.textSearchQuery(context, query).await();
						searches.add(getDocumentForSearchResult(document.getString("name"), latLng, query, response));

						Document searchDocument = new Document();
						searchDocument.append("name", document.getString("name"))
								.append("address", document.getString("address"))
								.append("latitude_longitude", latLng.toString())
								.append("searches", Arrays.asList(searches));
						searchData.replaceOne(new Document("name", document.get("name")), searchDocument,
								(new UpdateOptions()).upsert(true));
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

	private Document getDocumentForSearchResult(String originName, LatLng orgin, String id,
			PlacesSearchResponse response) {
		StringSimilarity similarity = new StringSimilarity();

		Document searchResultList = new Document().append("search", id);
		List<Document> results = new ArrayList<Document>();
		for (PlacesSearchResult result : response.results) {
			Document resultDoc = new Document();
			resultDoc.append("name", result.name);
			if (result.formattedAddress != null) {
				resultDoc.append("formatted_address", result.formattedAddress);
			}
			List<String> types = new ArrayList<String>();
			for (String type : result.types) {
				types.add(type);
			}
			resultDoc.append("types", Arrays.asList(types));
			if (result.scope != null) {
				resultDoc.append("scope", result.scope.name());
			}
			results.add(resultDoc);

			resultDoc.append("cosine_distance", similarity.cosineDistance(originName, result.name));
			resultDoc.append("jaro_distance", similarity.jaroDistance(originName, result.name));
			resultDoc.append("jarowinkler_distance", similarity.jaroWinklerDistance(originName, result.name));
			resultDoc.append("levenshtein_distance", similarity.levenshteinDistance(originName, result.name));
			resultDoc.append("distance_to_origin", distanceCalculator.distance(orgin, result.geometry.location));
		}
		searchResultList.append("results", Arrays.asList(results));

		return searchResultList;

	}
}
