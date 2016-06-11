package com.tripagor;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import com.mongodb.MongoClientURI;
import com.tripagor.cli.exporter.KeywordExporterSeleniumImpl;
import com.tripagor.locations.CityRepository;
import com.tripagor.locations.model.City;

public class HotelCitiesKeywordByCountryExport {
	private CityRepository cityRepository;
	private KeywordExporterSeleniumImpl exporter;

	@Before
	public void before() throws Exception {
		String mongoUri = "mongodb://localhost:27017/hotels";
		// String mongoUri =
		// "mongodb://admin:vsjl1801@db.tripagor.com/hotels?authMechanism=SCRAM-SHA-1&authSource=admin";

		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new MongoClientURI(mongoUri));
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
		cityRepository = new MongoRepositoryFactory(mongoTemplate).getRepository(CityRepository.class);
		exporter = new KeywordExporterSeleniumImpl("admin@pickito.de", "sensor2016", "c:\\temp\\keywords");
	}

	@Test
	public void doExport() throws Exception {
		List<City> cities = cityRepository.findByCountryCode("de");

		List<String> keywords = new LinkedList<>();
		for (City city : cities) {
			keywords.add("hotels " + city.getName());
		}

		List<File> files = exporter.doExport(keywords);

		for (File file : files) {

		}

	}

}
