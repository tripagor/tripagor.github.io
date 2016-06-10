package com.tripagor;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import com.mongodb.MongoClientURI;
import com.tripagor.cli.exporter.KeywordExporterSeleniumImpl;
import com.tripagor.locations.CityRepository;

public class HotelCitiesKeywordByCountryExport {
	private CityRepository cityRepository;
	private KeywordExporterSeleniumImpl exporter;

	@Before
	public void before() throws Exception {
		MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new MongoClientURI(
				"mongodb://admin:vsjl1801@db.tripagor.com/hotels?authMechanism=SCRAM-SHA-1&authSource=admin"));
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
		cityRepository = new MongoRepositoryFactory(mongoTemplate).getRepository(CityRepository.class);
		exporter = new KeywordExporterSeleniumImpl("admin@pickito.de", "sensor2016", "c:\\temp\\keywords");
	}

	@Test
	public void doExport() throws Exception {

	}

}
