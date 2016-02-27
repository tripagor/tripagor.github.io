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
import com.google.maps.model.PlaceType;
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
			//id	name	address	zip	city_hotel	cc1	ufi	class	currencycode	minrate	maxrate	preferred	nr_rooms	longitude	latitude	public_ranking	hotel_url	photo_url	desc_en	desc_fr	desc_es	desc_de	desc_nl	desc_it	desc_pt	desc_ja	desc_zh	desc_pl	desc_ru	desc_sv	desc_ar	desc_el	desc_no	city_unique	city_preferred	continent_id	review_score	review_nr	

			parser = new CSVParser(reader,
					CSVFormat.TDF.withHeader("id", "name", "address", "zip", "city_hotel", "cc1", "ufi", "class",
							"currencycode", "minrate", "maxrate", "preferred", "nr_rooms", "longitude", "latitude",
							"public_ranking", "hotel_url", "photo_url", "desc_en", "desc_fr", "desc_es", "desc_de",
							"desc_nl", "desc_it", "desc_pt", "desc_ja", "desc_zh", "desc_pl", "desc_ru", "desc_sv",
							"desc_ar", "desc_el", "desc_no", "city_unique", "city_preferred", "continent_id",
							"review_score", "review_nr"));
			int i =0;
			int j = 0;
			for (final CSVRecord record : parser) {
				if (!record.get("name").equals("name")) {
					final String name = record.get("name");
					final String city = record.get("city_hotel");
					final double longitude = Double.parseDouble(record.get("longitude"));
					final double latitude = Double.parseDouble(record.get("latitude"));

					PlacesSearchResult[] places = placeExtractor.getAddressDetails(PlaceType.LODGING,
							new LatLng(latitude, longitude), 30);
					boolean isMarked = false;
					for (PlacesSearchResult place : places) {

						int length = name.length();
						if (place.name.length() > name.length()) {
							length = place.name.length();
						}
						if (StringUtils.getLevenshteinDistance(place.name, name) / length < 0.2) {
							isMarked = true;
						}
					}

					if (!isMarked) {
						System.out.println("not marked: " + name + ", " + city);
						j++;
					} 
					i++;
				}
			}
			System.out.println(i+" "+j);;
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

}
