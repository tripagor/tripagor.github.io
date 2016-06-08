package com.tripagor.cli.exporter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import com.tripagor.google.api.model.Location;
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
	private GeoApiContext geoApiContext;

	public BookingComHotelRegionsExporter(File importFile, RegionRepository regionRepository,
			GeoApiContext geoApiContext) {
		this.importFile = importFile;
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
			for (int i = 0; i < rows.size(); i++) {
				if (i > 0) {
					try {
						String name = rows.get(i)[propMap.get("region_name")];
						String countryCode = rows.get(i)[propMap.get("country_code")];
						String regionType = rows.get(i)[propMap.get("region_type")];
						int numOfHotels = Integer.parseInt(rows.get(i)[propMap.get("number_of_hotels")]);

						Region region = regionRepository.findByNameAndCountryCode(name, countryCode);

						if (region == null) {
							GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, name)
									.resultType(AddressType.LOCALITY).await();
							for (GeocodingResult result : results) {
								LatLng latLng = result.geometry.location;
								Location location = new Location();
								location.setLat(latLng.lat);
								location.setLng(latLng.lng);

								region = new Region();
								region.setName(name);
								region.setCountryCode(countryCode);
								region.setNumOfHotels(numOfHotels);
								region.setType(regionType);
								region.setLocation(location);
								break;
							}
						} else {
							region.setNumOfHotels(numOfHotels);
							region.setType(regionType);
						}

						if (region != null) {
							regionRepository.save(region);
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
