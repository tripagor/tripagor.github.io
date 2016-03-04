package com.tripagor.importer;

import java.io.File;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class BookingComExporter {

	private final Logger logger = LoggerFactory.getLogger(BookingComExporter.class);

	public BookingComExporter() {
	}

	public void extract(File importFile, String uri) {

		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		MongoClient mongoClient = new MongoClient(mongoClientURI);

		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		TsvParser parser = new TsvParser(settings);

		List<String[]> rows = parser.parseAll(importFile);

		try {
			for (int i = 0; i < rows.size(); i++) {
				if (i > 0) {
					try {
						final String name = rows.get(i)[1];
						final String address = rows.get(i)[2];
						final String zip = rows.get(i)[3];
						final String city = rows.get(i)[4];
						final String country = new Locale("", (rows.get(i)[5]).toUpperCase())
								.getDisplayCountry(new Locale("en"));
						final String desc = rows.get(i)[18];
						final String url = rows.get(i)[16];
						final String imageUrl = rows.get(i)[17];
						final double longitude = Double.parseDouble(rows.get(i)[13]);
						final double latitude = Double.parseDouble(rows.get(i)[14]);
						System.out.println(name);
					} catch (Exception e) {
						continue;
					}
				}
			}
		} catch (Exception e) {
			logger.error("failed with {}", e);
		} finally {
		}

	}

}
