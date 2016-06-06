package com.tripagor.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import com.google.maps.GeoApiContext;
import com.mongodb.MongoClientURI;
import com.tripagor.cli.exporter.HotelMarkerCheckWorker;
import com.tripagor.markers.HotelMarkerRespository;

public class HotelMarkerCheckCli {

	private static Options options;

	public static void main(String[] args) {
		CommandLineParser parser = new DefaultParser();

		options = new Options();
		options.addOption("h", false, "this help");

		options.addOption("a", true, "url postfix");

		options.addOption("k", true, "google api key");

		options.addOption("d", true, "Uri Mongo DB");

		try {
			CommandLine cmd = parser.parse(options, args);

			String key = null;
			String postfix = "";
			String mongoUri = null;

			if (cmd.hasOption("h")) {
				help();
			}

			if (cmd.hasOption("k")) {
				key = cmd.getOptionValue("k");
			}
			if (cmd.hasOption("a")) {
				postfix = cmd.getOptionValue("a");
			}
			if (cmd.hasOption("d")) {
				mongoUri = cmd.getOptionValue("d");
			}
			HotelMarkerRespository hotelMarkerRepository = null;
			if (mongoUri != null) {
				MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new MongoClientURI(mongoUri));
				MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
				hotelMarkerRepository = new MongoRepositoryFactory(mongoTemplate).getRepository(HotelMarkerRespository.class);
			}

			if (key != null && hotelMarkerRepository != null) {
				new HotelMarkerCheckWorker(hotelMarkerRepository, new GeoApiContext().setApiKey(key), postfix).doCheck();
			} else {
				help();
			}
		} catch (Exception e) {
			System.err.println("An problem occured:" + e);
		}
	}

	private static void help() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp(HotelMarkerCheckCli.class.getName(), options);
		System.exit(0);
	}

}
