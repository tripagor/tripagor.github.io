package com.tripagor.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;

import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;

public class BookingComImporter {

	private PlaceExtractor placeExtractor;

	public BookingComImporter() {

		placeExtractor = new PlaceExtractor();
	}

	public void doImport(File file) throws RuntimeException {
		Reader reader = null;
		CSVParser parser = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
			// id name address zip city_hotel cc1 ufi class currencycode minrate
			// maxrate preferred nr_rooms longitude latitude public_ranking
			// hotel_url photo_url desc_en desc_fr desc_es desc_de desc_nl
			// desc_it desc_pt desc_ja desc_zh desc_pl desc_ru desc_sv desc_ar
			// desc_el desc_no city_unique city_preferred continent_id
			// review_score review_nr

			parser = new CSVParser(reader,
					CSVFormat.TDF.withHeader("id", "name", "address", "zip", "city_hotel", "cc1", "ufi", "class",
							"currencycode", "minrate", "maxrate", "preferred", "nr_rooms", "longitude", "latitude",
							"public_ranking", "hotel_url", "photo_url", "desc_en", "desc_fr", "desc_es", "desc_de",
							"desc_nl", "desc_it", "desc_pt", "desc_ja", "desc_zh", "desc_pl", "desc_ru", "desc_sv",
							"desc_ar", "desc_el", "desc_no", "city_unique", "city_preferred", "continent_id",
							"review_score", "review_nr"));
			int i = 0;
			int j = 0;
			for (final CSVRecord record : parser) {
				if (!record.get("name").equals("name")) {
					final String name = record.get("name");
					final String city = record.get("city_hotel");
					final String address = record.get("address");
					final String zip = record.get("zip");
					final double longitude = Double.parseDouble(record.get("longitude"));
					final double latitude = Double.parseDouble(record.get("latitude"));

					//PlacesSearchResult[] places = placeExtractor.findPlaces(new LatLng(latitude, longitude), 500);
					PlacesSearchResult[] places = placeExtractor.findPlaces(name);
					boolean isMarked = false;
					System.out.println("HOTEL=" + name + ", " + " address=" + address + "," + zip + " " + city
							+ " gemotery=" + new LatLng(latitude, longitude));
					for (PlacesSearchResult place : places) {

						System.out.println(">>>>>>>" + place.name + " isMatched?" + getWeight(place.name, name));
						if (getWeight(place.name, name) > 0.4) {
							isMarked = true;
							break;
						}
					}

					if (!isMarked) {
						j++;
					}
					System.out.println("isMarked?" + isMarked);
					System.out.println("============================");
					i++;
				}
			}
			System.out.println(i + " " + j);
			;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				parser.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private double getWeight(String str1, String str2) {
		// get the max possible levenstein distance score for string
		int maxLen = Math.max(str1.length(), str2.length());

		// check for 0 maxLen
		if (maxLen == 0) {
			return 1.0; // as both strings identically zero length
		} else {
			final int levenshteinDistance = StringUtils.getLevenshteinDistance(str1, str2);
			// return actual / possible levenstein distance to get 0-1 range
			return 1.0 - ((double) levenshteinDistance / maxLen);
		}
	}

}
