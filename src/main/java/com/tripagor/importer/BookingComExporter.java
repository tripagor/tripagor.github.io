package com.tripagor.importer;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class BookingComExporter {

	private final Logger logger = LoggerFactory.getLogger(BookingComExporter.class);

	public BookingComExporter() {
	}

	public void extract(Path path, String uri, String collectionname) {
		File directory = path.toFile();
		for (File file : directory.listFiles()) {
			System.out.println("extracting file "+file.getName()+"...");
			extract(file, uri, collectionname);
			System.out.println("extracting file "+file.getName()+" suceeded.");
		}
	}

	public void extract(File importFile, String uri, String collectionname) {

		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		MongoCollection<Document> collection = mongoClient.getDatabase(mongoClientURI.getDatabase())
				.getCollection(collectionname);

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
						collection.insertOne(new Document("name", name));
					} catch (Exception e) {
						continue;
					}
				} else {
					System.out.println(rows.get(i).toString());
				}
			}
		} catch (Exception e) {
			logger.error("failed with {}", e);
		} finally {
			mongoClient.close();
		}

	}

}
