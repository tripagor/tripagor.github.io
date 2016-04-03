package com.tripagor.cli.exporter;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.CaseFormat;
import com.tripagor.hotels.model.Hotel;
import com.tripagor.rest.RestTemplateFactory;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class BookingComExporter {

	private final Logger logger = LoggerFactory.getLogger(BookingComExporter.class);
	private final Map<Integer, String> propMap = new HashMap<Integer, String>();
	private final Map<String, String> propertyReplaceMap = new HashMap<String, String>();
	private RestTemplateFactory restTemplateFactory;
	private TsvParser parser;

	public BookingComExporter() {
		propertyReplaceMap.put("id", "booking_com_id");
		propertyReplaceMap.put("cc1", "country_code");
		restTemplateFactory = new RestTemplateFactory();

		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		parser = new TsvParser(settings);
	}

	public void extract(Path path, String restUri, String clientId, String clientSecret) {
		File directory = path.toFile();
		for (File file : directory.listFiles()) {
			System.out.println("extracting file " + file.getName() + "...");
			extract(file, restUri, clientId, clientSecret);
			System.out.println("extracting file " + file.getName() + " succeeded.");
		}
	}

	public void extract(File importFile, String host, String clientId, String clientSecret) {
		RestTemplate restTemplate = restTemplateFactory.get(host, clientId, clientSecret);

		List<String[]> rows = parser.parseAll(importFile);

		try {
			for (int i = 0; i < rows.size(); i++) {
				if (i > 0) {
					try {
						String[] values = rows.get(i);

						Map<String, Object> valueMap = new HashMap<>();
						for (int j = 0; j < values.length; j++) {
							if (values[j] != null) {
								valueMap.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, propMap.get(j)),
										toObject(getType(propMap.get(j)), values[j]));
							}
						}

						Hotel hotel = new Hotel();
						BeanUtils.populate(hotel, valueMap);
						hotel.setCity(valueMap.get("cityHotel").toString());
						hotel.setUrl(valueMap.get("hotelUrl").toString());
						hotel.setImageUrl(valueMap.get("photoUrl").toString());

						Hotel loaded = restTemplate.getForObject(
								host.concat("/hotels/search/findByBookingComId?bookingId={bookingComId}"), Hotel.class,
								hotel.getBookingComId());
						if (loaded == null) {
							restTemplate.postForObject(host.concat("/hotels"), hotel, Hotel.class);
						} else {
							HttpHeaders headers = new HttpHeaders();
							headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
							HttpEntity<Hotel> requestEntity = new HttpEntity<>(hotel, headers);
							restTemplate.exchange(host.concat("/hotels/{id}"), HttpMethod.PATCH, requestEntity,
									String.class, loaded.getId());
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
