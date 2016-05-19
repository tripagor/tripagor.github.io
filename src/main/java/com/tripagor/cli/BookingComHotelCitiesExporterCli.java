package com.tripagor.cli;

import java.io.File;

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
import com.tripagor.cli.exporter.BookingComHotelCitiesExporter;
import com.tripagor.locations.CityRepository;

public class BookingComHotelCitiesExporterCli {
	private static Options options;

	public static void main(String args[]) {

		options = new Options();

		options.addOption("h", false, "this help");

		options.addOption("s", true, "source file");

		options.addOption("d", true, "Uri Mongo DB");

		options.addOption("k", true, "Google places Api key");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			String source = null;
			String mongoUri = null;
			String key = null;

			if (cmd.hasOption("h")) {
				help();
			}
			if (cmd.hasOption("s")) {
				source = cmd.getOptionValue("s");
			}
			if (cmd.hasOption("d")) {
				mongoUri = cmd.getOptionValue("d");
			}
			if (cmd.hasOption("k")) {
				key = cmd.getOptionValue("k");
			}

			if (source != null && mongoUri != null && key != null) {
				MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new MongoClientURI(mongoUri));
				MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
				CityRepository cityRepository = new MongoRepositoryFactory(mongoTemplate)
						.getRepository(CityRepository.class);
				new BookingComHotelCitiesExporter(new File(source), cityRepository, new GeoApiContext().setApiKey(key))
						.doExport();
			} else {
				help();
			}
		} catch (Exception e) {
			help();
		}

	}

	private static void help() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp(HotelMarkerCheckCli.class.getName(), options);
		System.exit(0);
	}
}
