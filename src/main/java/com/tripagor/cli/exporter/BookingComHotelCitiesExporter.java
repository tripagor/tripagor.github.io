package com.tripagor.cli.exporter;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripagor.locations.City;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class BookingComHotelCitiesExporter {
	private File importFile;
	private TsvParser parser;
	private final Logger logger = LoggerFactory.getLogger(BookingComHotelCitiesExporter.class);
	private Map<String, Integer> propMap;

	public BookingComHotelCitiesExporter(File importFile) {
		this.importFile = importFile;
		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		this.parser = new TsvParser(settings);
		this.propMap = new HashMap<>();
	}

	public Collection<City> doExport() {
		Collection<City> cities = new LinkedList<>();

		List<String[]> rows = parser.parseAll(importFile);

		try {
			for (int i = 0; i < rows.size(); i++) {
				if (i > 0) {
					try {
						City city = new City();
						city.setName(rows.get(i)[propMap.get("full_name")]);
						city.setCountryCode(rows.get(i)[propMap.get("country_code")]);
						//city.setNumOfHotels(rows.get(i)[Integer.parseInt(rows.get(i)[propMap.get("number_of_hotels")]);
					} catch (Exception e) {
						continue;
					}
				} else {
					String[] propertyNames = rows.get(i);
					for (int j = 0; j < propertyNames.length; j++) {
						String propertyName = propertyNames[j];
						propMap.put(propertyName, i);
					}
				}
			}
		} catch (Exception e) {
			logger.error("failed with {}", e);
		}

		return cities;
	}

}
