package com.tripagor.cli.exporter;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import com.mongodb.MongoClientURI;
import com.tripagor.hotels.HotelRepository;
import com.tripagor.locations.CityRepository;
import com.tripagor.locations.Region;
import com.tripagor.locations.RegionRepository;

public class DataPointExporter {

	@Test
	public void doExport() throws Exception {

		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(
				new MongoClientURI("mongodb://localhost:27017/hotels"));
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
		CityRepository cityRepository = new MongoRepositoryFactory(mongoTemplate).getRepository(CityRepository.class);
		HotelRepository hotelRepository = new MongoRepositoryFactory(mongoTemplate)
				.getRepository(HotelRepository.class);
		RegionRepository regionRepository = new MongoRepositoryFactory(mongoTemplate)
				.getRepository(RegionRepository.class);

		List<Region> regions = regionRepository.findAll();

		double max = 0;

		for (Region region : regions) {
			if (region.getNumOfHotels() > max) {
				max = region.getNumOfHotels();
			}
		}

		String jsonStr = "[";
		for (Region region : regions) {
			if (region.getLocation() != null) {
				jsonStr += "[";
				double weight = region.getNumOfHotels() / max;
				jsonStr += region.getLocation().getLat() + "," + region.getLocation().getLng() + "," + weight;
				jsonStr += "],";
			}
		}

		jsonStr += "]";
		Files.write(Paths.get("src/main/resources/static/web/json", "regions.json"), jsonStr.getBytes());
	}

}
