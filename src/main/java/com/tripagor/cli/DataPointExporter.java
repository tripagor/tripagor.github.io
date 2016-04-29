package com.tripagor.cli;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import com.mongodb.MongoClientURI;
import com.tripagor.hotels.HotelRepository;
import com.tripagor.hotels.model.Hotel;
import com.tripagor.locations.CityRepository;
import com.tripagor.locations.Region;
import com.tripagor.locations.RegionRepository;

public class DataPointExporter {

	public static void main(String args[]) throws Exception {

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
				double weight = new BigDecimal(region.getNumOfHotels())
						.divide(new BigDecimal(max), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
				jsonStr += region.getLocation().getLat() + "," + region.getLocation().getLng() + "," + weight;
				jsonStr += "],";
			}
		}

		jsonStr += "]";
		Files.write(Paths.get("src/main/resources/static/web/json", "regions.json"), jsonStr.getBytes());

		String[] locales = Locale.getISOCountries();

		for (String countryCode : locales) {
			jsonStr = "[";
			int numOfPages = 1;
			int currentPage = 0;
			while (currentPage < numOfPages) {
				Page<Hotel> page = hotelRepository.findByCountryCode(countryCode.toLowerCase(),
						new PageRequest(currentPage++, 500));
				numOfPages = page.getTotalPages();
				for (Hotel hotel : page.getContent()) {
					jsonStr += "[";
					jsonStr += hotel.getLatitude() + "," + hotel.getLongitude();
					jsonStr += "],";
				}
			}
			jsonStr += "]";

			Files.write(Paths.get("src/main/resources/static/web/json", "hotels_" + countryCode + ".json"),
					jsonStr.getBytes());
		}
	}

}
