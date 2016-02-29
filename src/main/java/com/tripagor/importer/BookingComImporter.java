package com.tripagor.importer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import com.google.maps.model.PlacesSearchResult;

public class BookingComImporter {

	private PlaceService placeExtractor;

	public BookingComImporter() {

		placeExtractor = new PlaceService();
	}

	public void doImport(File file) throws RuntimeException {
		ICsvMapReader mapReader = null;
		try {
			mapReader = new CsvMapReader(new FileReader(file), CsvPreference.TAB_PREFERENCE);

			// the header elements are used to map the values to the bean (names
			// must match)
			final String[] header = mapReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();
			Map<String, Object> customerMap;
			int i = 0;
			int j = 0;
			while ((customerMap = mapReader.read(header, processors)) != null) {

				final String name = (String) customerMap.get("name");
				final String city = (String) customerMap.get("city_hotel");
				final String address = (String) customerMap.get("address");
				final String zip = (String) customerMap.get("zip");
				final double longitude = Double.parseDouble((String) customerMap.get("longitude"));
				final double latitude = Double.parseDouble((String) customerMap.get("latitude"));

				System.out.println("HOTEL=" + name + ", " + " address=" + address + "," + zip + " " + city
						+ " gemotery=" + latitude + "," + longitude);

				PlacesSearchResult[] places = placeExtractor.findPlaces(name);
				boolean isMarked = false;
				for (PlacesSearchResult place : places) {
					System.out.println(">>>>>>>" + place.name + " isMatched?" + getWeightJaroWinkler(place.name, name));
					if (getWeightJaroWinkler(place.name, name) > 0.3) {
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
			System.out.println(i + " " + j);

		} catch (Exception e) {
			System.out.println("could not read file");
		} finally {

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
