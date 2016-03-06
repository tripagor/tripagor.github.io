package com.tripagor.importer;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.model.PlacesSearchResult;
import com.tripagor.importer.model.Address;
import com.tripagor.importer.model.Lodging;
import com.tripagor.service.AddressNormalizer;
import com.tripagor.service.PlaceService;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class BookingComUmarkedPlaceFinder {

	private PlaceService placeExtractor;
	private final Logger logger = LoggerFactory.getLogger(BookingComUmarkedPlaceFinder.class);
	private ObjectMapper mapper;
	private StringSimilarity stringComparisonWeight;
	private AddressNormalizer addressNormalizer;
	private int maxImports = 100000;

	public BookingComUmarkedPlaceFinder(PlaceService placeExtractor, StringSimilarity stringComparisonWeight,
			int maxImports) {
		super();
		this.placeExtractor = placeExtractor;
		this.stringComparisonWeight = stringComparisonWeight;
		this.maxImports = maxImports;
		this.addressNormalizer = new AddressNormalizer();
	}

	public BookingComUmarkedPlaceFinder() {
		mapper = new ObjectMapper();
		placeExtractor = new PlaceService();
		stringComparisonWeight = new StringSimilarity();
		this.addressNormalizer = new AddressNormalizer();
	}

	public void extract(File importFile, File exportFile) {

		PrintWriter printWriter = null;
		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		TsvParser parser = new TsvParser(settings);

		List<String[]> rows = parser.parseAll(importFile);

		try {
			printWriter = new PrintWriter(exportFile);
			printWriter.print("[");
			boolean hasEntry = false;
			int numberOfImportedRows = 0;
			for (int i = 0; i < rows.size(); i++) {
				if (i > 0) {
					if (numberOfImportedRows >= maxImports) {
						break;
					}
					numberOfImportedRows++;

					final String name = new String(rows.get(i)[1].getBytes(), "UTF-8");
					final String address = new String(rows.get(i)[2].getBytes(), "UTF-8");
					final String zip = new String(rows.get(i)[3].getBytes(), "UTF-8");
					final String city = new String(rows.get(i)[4].getBytes(), "UTF-8");
					final String country = new Locale("", (rows.get(i)[5]).toUpperCase())
							.getDisplayCountry(new Locale("en"));
					final String desc = new String(rows.get(i)[18].getBytes(),"UTF-8");
					final String url = new String(rows.get(i)[16].getBytes(),"UTF-8");
					final String imageUrl = rows.get(i)[17];
					final double longitude = Double.parseDouble(rows.get(i)[13]);
					final double latitude = Double.parseDouble(rows.get(i)[14]);

					PlacesSearchResult[] places = placeExtractor.find(name);
					boolean isMarked = false;
					for (PlacesSearchResult place : places) {
						if (stringComparisonWeight.cosineDistance(place.name, name) > 0.3) {
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
						accommodation.getAddress()
								.setWellFormattedAddress(addressNormalizer.wellFormattedString(latitude, longitude));
						accommodation.setDescription(desc);
						accommodation.setUrl(url);
						accommodation.getImageUrls().add(imageUrl);
						printWriter.print(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(accommodation));
					}
				}

			}
		} catch (Exception e) {
			logger.error("failed with {}", e);
		} finally {
			printWriter.print("]");
			printWriter.close();
		}

	}

	public void setMaxImports(int maxImports) {
		this.maxImports = maxImports;
	}

}
