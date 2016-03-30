package com.tripagor.cli.exporter;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import com.tripagor.hotels.model.Hotel;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class BookingComExporter {

	private final Logger logger = LoggerFactory.getLogger(BookingComExporter.class);
	private final Map<Integer, String> propMap = new HashMap<Integer, String>();
	private final Map<String, String> propertyReplaceMap = new HashMap<String, String>();
	private RestTemplate restTemplate;

	public BookingComExporter() {
		propertyReplaceMap.put("id", "booking_com_id");
		propertyReplaceMap.put("cc1", "country_code");
		restTemplate = new RestTemplate();
	}

	public void extract(Path path, String uri, String collectionname) {
		File directory = path.toFile();
		for (File file : directory.listFiles()) {
			System.out.println("extracting file " + file.getName() + "...");
			extract(file, uri, collectionname);
			System.out.println("extracting file " + file.getName() + " suceeded.");
		}
	}

	public void extract(Path path, String restUri, String clientId, String clientSecret) {
		File directory = path.toFile();
		for (File file : directory.listFiles()) {
			System.out.println("extracting file " + file.getName() + "...");
			extract(file, restUri, clientId, clientSecret);
			System.out.println("extracting file " + file.getName() + " suceeded.");
		}
	}

	public void extract(File importFile, String restUri, String clientId, String clientSecret) {

		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		TsvParser parser = new TsvParser(settings);

		List<String[]> rows = parser.parseAll(importFile);

		try {
			for (int i = 0; i < rows.size(); i++) {
				if (i > 0) {
					try {
						// {0=booking_com_id, 1=name, 2=address, 3=zip,
						// 4=city_hotel, 5=country_code, 6=ufi, 7=class,
						// 8=currencycode, 9=minrate, 10=maxrate, 11=preferred,
						// 12=nr_rooms, 13=longitude, 14=latitude,
						// 15=public_ranking, 16=hotel_url, 17=photo_url,
						// 18=desc_en, 19=desc_fr, 20=desc_es, 21=desc_de,
						// 22=desc_nl,
						// 23=desc_it, 24=desc_pt, 25=desc_ja, 26=desc_zh,
						// 27=desc_pl, 28=desc_ru, 29=desc_sv, 30=desc_ar,
						// 31=desc_el, 32=desc_no, 33=city_unique,
						// 34=city_preferred, 35=continent_id, 36=review_score,
						// 37=review_nr, 38=null}
						String[] values = rows.get(i);

						Map<String, Object> valueMap = new HashMap<>();
						for (int j = 0; j < values.length; j++) {
							if (values[j] != null) {
								valueMap.put(propMap.get(j), toObject(getType(propMap.get(j)), values[j]));
							}
						}

						Hotel hotel = new Hotel();
						if (propMap.get("id") != null) {
							hotel.setBookingComId(Long.parseLong(propMap.get("id")));
						}
						hotel.setName(propMap.get("name"));
						hotel.setAddress(propMap.get("address"));
						hotel.setZip(propMap.get("zip"));
						hotel.setCity(propMap.get("city_hotel"));
						hotel.setCountryCode(propMap.get("cc1"));
						hotel.setUfi(propMap.get("ufi"));
						if (propMap.get("hotelClass") != null) {
							hotel.setHotelClass(Integer.parseInt(propMap.get("hotelClass")));
						}
						hotel.setCurrencycode(propMap.get("currencycode"));
						if (propMap.get("minrate") != null) {
							hotel.setMinrate(Double.parseDouble(propMap.get("minrate")));
						}
						if (propMap.get("maxrate") != null) {
							hotel.setMaxrate(Double.parseDouble(propMap.get("maxrate")));
						}
						if (propMap.get("nr_rooms") != null) {
							hotel.setNrRooms(Integer.parseInt(propMap.get("nr_rooms")));
						}
						hotel.setLongitude(propMap.get("longitude"));
						hotel.setLatitude(propMap.get("latitude"));
						if (propMap.get("public_ranking") != null) {
							hotel.setPublicRanking(Double.parseDouble(propMap.get("public_ranking")));
						}
						hotel.setUrl(propMap.get("hotel_url"));
						hotel.setImageUrl(propMap.get("photo_url"));
						hotel.setDescEn(propMap.get("desc_en"));
						hotel.setDescFr(propMap.get("desc_fr"));
						hotel.setDescEs(propMap.get("desc_es"));
						hotel.setDescDe(propMap.get("desc_de"));
						hotel.setDescNl(propMap.get("desc_nl"));
						hotel.setDescIt(propMap.get("desc_it"));
						hotel.setDescPt(propMap.get("desc_pt"));
						hotel.setDescJa(propMap.get("desc_ja"));
						hotel.setDescZh(propMap.get("desc_zh"));
						hotel.setDescPl(propMap.get("desc_pl"));
						hotel.setDescRu(propMap.get("desc_ru"));
						hotel.setDescSv(propMap.get("desc_sv"));
						hotel.setDescAr(propMap.get("desc_ar"));
						hotel.setDescEl(propMap.get("desc_el"));
						hotel.setDescNo(propMap.get("desc_no"));
						hotel.setCityUnique(propMap.get("city_unique"));
						hotel.setCityPreferred(propMap.get("city_preferred"));
						if (propMap.get("continent_id") != null) {
							hotel.setContinentId(Integer.parseInt(propMap.get("continent_id")));
						}
						if (propMap.get("review_score") != null) {
							hotel.setReviewScore(Double.parseDouble(propMap.get("review_score")));
						}
						if (propMap.get("review_nr") != null) {
							hotel.setReviewNr(Integer.parseInt(propMap.get("review_nr")));
						}
						if (propMap.get("preferred") != null) {
							hotel.setPreferredBookingComPartner(Integer.parseInt(propMap.get("preferred")));
						}

						Hotel loaded = restTemplate.getForObject(
								restUri.concat("/search/findByBookingComId?bookingId={bookingComId}"), Hotel.class,
								hotel.getBookingComId());
						if (loaded == null) {
							restTemplate.postForObject(restUri, hotel, Hotel.class);
						} else {
							HttpEntity<Hotel> requestEntity = new HttpEntity<>(hotel);
							restTemplate.exchange(restUri.concat("/{bookingComId}"), HttpMethod.PATCH, requestEntity,
									Void.class);
						}

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
			return Double.class;
		} else if ("maxrate".equals(columnName)) {
			return Double.class;
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
