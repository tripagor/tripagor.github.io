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
import com.tripagor.locations.CityRepository;
import com.tripagor.locations.Region;
import com.tripagor.locations.RegionRepository;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

public class BookingComHotelRegionsExporter {
	private File importFile;
	private TsvParser parser;
	private final Logger logger = LoggerFactory.getLogger(BookingComHotelRegionsExporter.class);
	private Map<String, Integer> propMap;
	private RegionRepository regionRepository;

	public BookingComHotelRegionsExporter(File importFile, RegionRepository regionRepository) {
		this.importFile = importFile;
		this.regionRepository = regionRepository;
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
						Region region = new Region();
						region.setName(rows.get(i)[propMap.get("region_name")]);
						region.setCountryCode(rows.get(i)[propMap.get("country_code")]);
						region.setNumOfHotels(Integer.parseInt(rows.get(i)[propMap.get("number_of_hotels")]));
						
						regionRepository.save(region);						
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

		return cities;
	}

}
