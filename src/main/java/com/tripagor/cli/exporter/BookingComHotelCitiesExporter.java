package com.tripagor.cli.exporter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripagor.locations.City;
import com.tripagor.locations.CityRepository;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class BookingComHotelCitiesExporter {
	private File importFile;
	private TsvParser parser;
	private final Logger logger = LoggerFactory.getLogger(BookingComHotelCitiesExporter.class);
	private Map<String, Integer> propMap;
	private CityRepository cityRepository;

	public BookingComHotelCitiesExporter(File importFile, CityRepository cityRepository) {
		this.importFile = importFile;
		this.cityRepository = cityRepository;
		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		this.parser = new TsvParser(settings);
		this.propMap = new HashMap<>();
	}

	public void doExport() {
		List<String[]> rows = parser.parseAll(importFile);

		try {
			for (int i = 0; i < rows.size(); i++) {
				if (i > 0) {
					try {
						String name = rows.get(i)[propMap.get("full_name")];
						String countryCode = rows.get(i)[propMap.get("country_code")];
						int numOfHotels = Integer.parseInt(rows.get(i)[propMap.get("number_of_hotels")]);

						City city = cityRepository.findByNameAndCountryCode(name, countryCode);

						if (city == null) {
							city = new City();
							city.setName(name);
							city.setCountryCode(countryCode);
							city.setNumOfHotels(numOfHotels);
						} else {
							city.setNumOfHotels(numOfHotels);
						}

						cityRepository.save(city);

					} catch (Exception e) {
						continue;
					}
				} else {
					String[] propertyNames = rows.get(i);
					for (int j = 0; j < propertyNames.length; j++) {
						String propertyName = propertyNames[j];
						propMap.put(propertyName, j);
					}
				}
			}
		} catch (Exception e) {
			logger.error("failed with {}", e);
		}
	}

}
