package com.tripagor.importer;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class BookingComExporter {

	private final Logger logger = LoggerFactory.getLogger(BookingComExporter.class);
	private final Map<Integer, String> propMap = new HashMap<Integer, String>();
	private final Map<String, String> propertyReplaceMap = new HashMap<String, String>();

	public BookingComExporter() {
		propertyReplaceMap.put("id", "booking_com_id");
		propertyReplaceMap.put("cc1", "country_code");
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
		collection.createIndex(new Document("booking_com_id", 1));
		collection.createIndex(new Document("country_code", 1));
		collection.createIndex(new Document("city_unique", 1));
		collection.createIndex(new Document("continent_id", 1));

		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		TsvParser parser = new TsvParser(settings);

		List<String[]> rows = parser.parseAll(importFile);

		try {
			for (int i = 0; i < rows.size(); i++) {
				if (i > 0) {
					try {

						Document document = new Document();
						String[] values = rows.get(i);
						for (int j = 0; j < values.length; j++) {
							if (values[j] != null) {
								document.append(propMap.get(j), toObject(getType(propMap.get(j)), values[j]));
							}
						}

						collection.replaceOne(new Document("booking_com_id", document.get("booking_com_id")), document,
								(new UpdateOptions()).upsert(true));
					} catch (Exception e) {
						continue;
					}
				} else {
					String[] propertyNames = rows.get(i);
					for (int j = 0; j < propertyNames.length; j++) {
						String propertyName = propertyNames[j];
						if (this.propertyReplaceMap.get(propertyName) != null) {
							propertyName = this.propertyReplaceMap.get(propertyName);
						}
						propMap.put(j, propertyName);
					}
				}
			}
		} catch (Exception e) {
			logger.error("failed with {}", e);
		} finally {
			mongoClient.close();
		}

	}

	@SuppressWarnings("rawtypes")
	public Class getType(String columnName) {
		Class clazz = String.class;
		if ("booking_com_id".equals(columnName)) {
			return Long.class;
		} else if ("minrate".equals(columnName)) {
			return Float.class;
		} else if ("maxrate".equals(columnName)) {
			return Float.class;
		} else if ("nr_rooms".equals(columnName)) {
			return Integer.class;
		} else if ("class".equals(columnName)) {
			return Double.class;
		} else if ("longitude".equals(columnName)) {
			return String.class;
		} else if ("latitude".equals(columnName)) {
			return String.class;
		} else if ("public_ranking".equals(columnName)) {
			return Double.class;
		} else if ("public_ranking".equals(columnName)) {
			return Integer.class;
		} else if ("continent_id".equals(columnName)) {
			return Byte.class;
		} else if ("review_score".equals(columnName)) {
			return Double.class;
		} else if ("review_nr".equals(columnName)) {
			return Integer.class;
		}
		return clazz;
	}

	public Object toObject(@SuppressWarnings("rawtypes") Class clazz, String value) {
		if (Boolean.class == clazz)
			return Boolean.parseBoolean(value);
		else if (Byte.class == clazz)
			return Byte.parseByte(value);
		else if (Short.class == clazz)
			return Short.parseShort(value);
		else if (Integer.class == clazz)
			return Integer.parseInt(value);
		else if (Long.class == clazz)
			return Long.parseLong(value);
		else if (Float.class == clazz)
			return Float.parseFloat(value);
		else if (Double.class == clazz)
			return Double.parseDouble(value);
		else if (BigDecimal.class == clazz)
			return new BigDecimal(value).doubleValue();
		else if (String.class == clazz)
			return value;
		return value;
	}

}
