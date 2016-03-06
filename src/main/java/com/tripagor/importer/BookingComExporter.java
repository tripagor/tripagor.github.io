package com.tripagor.importer;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.InsertOneOptions;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class BookingComExporter {

	private final Logger logger = LoggerFactory.getLogger(BookingComExporter.class);
	private final Map<Integer, String> propMap = new HashMap<Integer, String>();

	public BookingComExporter() {
	}

	public void extract(Path path, String uri, String collectionname) {
		File directory = path.toFile();
		for (File file : directory.listFiles()) {
			System.out.println("extracting file " + file.getName() + "...");
			extract(file, uri, collectionname);
			System.out.println("extracting file " + file.getName() + " suceeded.");
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

						Document document = new Document();
						document.append("is_evaluated", false);
						String[] values = rows.get(i);
						for (int j = 0; j < values.length; j++) {
							if (values[j] != null) {
								document.append(propMap.get(j), values[j]);
							}
						}

						collection.insertOne(document);
					} catch (Exception e) {
						continue;
					}
				} else {
					String[] propertyNames = rows.get(i);
					for (int j = 0; j < propertyNames.length; j++) {
						propMap.put(j, propertyNames[j]);
					}
					System.out.println(propMap);
				}
			}
		} catch (Exception e) {
			logger.error("failed with {}", e);
		} finally {
			mongoClient.close();
		}

	}

}
