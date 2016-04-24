package com.tripagor.cli.exporter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.maps.GeoApiContext;
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
	private File exportFolder;
	private GeoApiContext geoApiContext;

	public BookingComHotelRegionsExporter(File importFile, File exportFolder, RegionRepository regionRepository,
			GeoApiContext geoApiContext) {
		this.importFile = importFile;
		this.exportFolder = exportFolder;
		this.regionRepository = regionRepository;
		this.geoApiContext = geoApiContext;

		TsvParserSettings settings = new TsvParserSettings();
		settings.getFormat().setLineSeparator("\n");
		this.parser = new TsvParser(settings);
		this.propMap = new HashMap<>();
	}

	public void doExport() {
		List<String[]> rows = parser.parseAll(importFile);

		try {
			List<String> keywords = new LinkedList<>();
			int filenameCounter = 1;
			String filename = "googleKeywordRegion";
			for (int i = 0; i < rows.size(); i++) {
				if (i > 0) {
					try {
						String name = rows.get(i)[propMap.get("region_name")];
						String countryCode = rows.get(i)[propMap.get("country_code")];
						String regionType = rows.get(i)[propMap.get("region_type")];
						int numOfHotels = Integer.parseInt(rows.get(i)[propMap.get("number_of_hotels")]);

						Region region = regionRepository.findByNameAndCountryCode(name, countryCode);

						if (region == null) {
							region = new Region();
							region.setName(name);
							region.setCountryCode(countryCode);
							region.setNumOfHotels(numOfHotels);
							region.setType(regionType);
						} else {
							region.setNumOfHotels(numOfHotels);
							region.setType(regionType);
						}

						regionRepository.save(region);

						keywords.add("hotels " + name);
						if (keywords.size() == 800 || i == (rows.size() - 1)) {
							String nameStr = "";
							for (String keyword : keywords) {
								nameStr += keyword + "\n";
							}
							Files.write(Paths.get(Paths.get(exportFolder.getAbsolutePath()).toString(),
									filename + "_" + filenameCounter + ".csv"), nameStr.getBytes());
							filenameCounter++;
							keywords = new LinkedList<>();
						}
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
