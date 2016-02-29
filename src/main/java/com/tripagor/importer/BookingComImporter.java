package com.tripagor.importer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.PlacesSearchResult;
import com.tripagor.importer.model.Accommodation;
import com.tripagor.importer.model.Address;

public class BookingComImporter {

	private PlaceService placeExtractor;
	private final Logger logger = LoggerFactory.getLogger(BookingComImporter.class);
	private ObjectMapper mapper;

	public BookingComImporter() {

		mapper = new ObjectMapper();
		placeExtractor = new PlaceService();
	}

	public void doImport(File importFile, File exportFile) throws RuntimeException {
		ICsvMapReader mapReader = null;
		PrintWriter printWriter = null;
		try {
			mapReader = new CsvMapReader(new FileReader(importFile), CsvPreference.TAB_PREFERENCE);
			printWriter = new PrintWriter(exportFile);
			printWriter.print("[");
			/*
			 * id name address zip city_hotel cc1 ufi class currencycode minrate
			 * maxrate preferred nr_rooms longitude latitude public_ranking
			 * hotel_url photo_url desc_en desc_fr desc_es desc_de desc_nl
			 * desc_it desc_pt desc_ja desc_zh desc_pl desc_ru desc_sv desc_ar
			 * desc_el desc_no city_unique city_preferred continent_id
			 * review_score review_nr
			 */
			// the header elements are used to map the values to the bean (names
			// must match)
			final String[] header = mapReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();
			Map<String, Object> customerMap;
			try {
				boolean hasEntry = false;
				while ((customerMap = mapReader.read(header, processors)) != null) {
					final String name = (String) customerMap.get("name");
					final String city = (String) customerMap.get("city_hotel");
					final String address = (String) customerMap.get("address");
					final String zip = (String) customerMap.get("zip");
					final String desc = (String) customerMap.get("desc_en");
					final String url = (String) customerMap.get("hotel_url");
					final String imageUrl = (String) customerMap.get("photo_url");
					final String country = new Locale("", ((String) customerMap.get("cc1")).toUpperCase())
							.getDisplayCountry(new Locale("en"));
					final double longitude = Double.parseDouble((String) customerMap.get("longitude"));
					final double latitude = Double.parseDouble((String) customerMap.get("latitude"));

					PlacesSearchResult[] places = placeExtractor.findPlaces(name);
					boolean isMarked = false;
					for (PlacesSearchResult place : places) {
						if (getWeightJaroWinkler(place.name, name) > 0.3) {
							isMarked = true;
							break;
						}
					}

					if (!isMarked) {
						if (hasEntry) {
							printWriter.print(",");
						}
						hasEntry = true;
						logger.debug("HOTEL=" + name + ", " + " address=" + address + "," + zip + " " + city
								+ " gemotery=" + latitude + "," + longitude);
						Accommodation accommodation = new Accommodation();
						accommodation.setName(name);
						accommodation.setAddress(new Address(address, zip, city, country, "", longitude, latitude));
						accommodation.setDescription(desc);
						accommodation.setUrl(url);
						accommodation.getImageUrls().add(imageUrl);
						printWriter.print(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accommodation));
					}
				}
			} catch (Exception e) {
				logger.error("failed with {}", e);
			}

		} catch (Exception e) {
			logger.error("failed with {}", e);
		} finally {
			printWriter.print("]");
			printWriter.close();
			if (mapReader != null) {
				try {
					mapReader.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private static CellProcessor[] getProcessors() {
		final CellProcessor[] processors = new CellProcessor[] { new Optional(), new Optional(), new Optional(),
				new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(),
				new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(),
				new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(),
				new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(),
				new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional(),
				new Optional(), new Optional(), new Optional(), new Optional(), new Optional(), new Optional() };

		return processors;
	}

	private double getWeightLevenshtein(String str1, String str2) {
		// get the max possible levenstein distance score for string
		int maxLen = Math.max(str1.length(), str2.length());

		// check for 0 maxLen
		if (maxLen == 0) {
			return 1.0; // as both strings identically zero length
		} else {
			final int distance = StringUtils.getLevenshteinDistance(str1, str2);
			// return actual / possible levenstein distance to get 0-1 range
			return 1.0 - ((double) distance / maxLen);
		}
	}

	private double getWeightFuzzy(String str1, String str2) {
		// get the max possible levenstein distance score for string
		int maxLen = Math.max(str1.length(), str2.length());

		// check for 0 maxLen
		if (maxLen == 0) {
			return 1.0; // as both strings identically zero length
		} else {
			final int distance = StringUtils.getFuzzyDistance(str1, str2, new Locale("en", "US"));
			// return actual / possible levenstein distance to get 0-1 range
			return 1.0 - ((double) distance / maxLen);
		}
	}

	private double getWeightJaroWinkler(String str1, String str2) {
		// get the max possible levenstein distance score for string
		int maxLen = Math.max(str1.length(), str2.length());

		// check for 0 maxLen
		if (maxLen == 0) {
			return 1.0; // as both strings identically zero length
		} else {
			final double distance = StringUtils.getJaroWinklerDistance(str1, str2);
			// return actual / possible levenstein distance to get 0-1 range
			return 1.0 - ((double) distance / maxLen);
		}
	}

}
