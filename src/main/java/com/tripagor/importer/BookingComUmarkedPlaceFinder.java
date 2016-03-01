package com.tripagor.importer;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.PlacesSearchResult;
import com.tripagor.importer.model.Lodging;
import com.tripagor.service.PlaceService;
import com.tripagor.importer.model.Address;

public class BookingComUmarkedPlaceFinder {

	private PlaceService placeExtractor;
	private final Logger logger = LoggerFactory.getLogger(BookingComUmarkedPlaceFinder.class);
	private ObjectMapper mapper;
	private StringComparisonWeight stringComparisonWeight;
	private int maxImports = 100000;

	public BookingComUmarkedPlaceFinder(PlaceService placeExtractor, StringComparisonWeight stringComparisonWeight,
			int maxImports) {
		super();
		this.placeExtractor = placeExtractor;
		this.stringComparisonWeight = stringComparisonWeight;
		this.maxImports = maxImports;
	}

	public BookingComUmarkedPlaceFinder() {
		mapper = new ObjectMapper();
		placeExtractor = new PlaceService();
		stringComparisonWeight = new StringComparisonWeight();
	}

	public void extract(File importFile, File exportFile) {
		ICsvMapReader mapReader = null;
		PrintWriter printWriter = null;
		try {
			mapReader = new CsvMapReader(new FileReader(importFile), CsvPreference.TAB_PREFERENCE);
			printWriter = new PrintWriter(exportFile);
			printWriter.print("[");

			final String[] header = mapReader.getHeader(true);
			final CellProcessor[] processors = getProcessors();
			Map<String, Object> customerMap;
			boolean hasEntry = false;
			int numberOfImportedRows = 0;

			try {
				while ((customerMap = mapReader.read(header, processors)) != null) {
					if (numberOfImportedRows >= maxImports) {
						break;
					}
					numberOfImportedRows++;

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
						if (stringComparisonWeight.getWeightJaroWinkler(place.name, name) > 0.3) {
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
						Lodging accommodation = new Lodging();
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

	public void setMaxImports(int maxImports) {
		this.maxImports = maxImports;
	}

}
