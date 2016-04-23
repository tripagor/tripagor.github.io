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

import com.mongodb.MongoClientURI;
import com.tripagor.cli.exporter.BookingComHotelCitiesExporter;
import com.tripagor.locations.CityRepository;

public class BookingComHotelCitiesExporterCli {
	private static Options options;

	public static void main(String args[]) {

		options = new Options();

		options.addOption("h", false, "this help");

		options.addOption("s", true, "source file");

		options.addOption("t", true, "target folder for keyword files");

		options.addOption("d", true, "Uri Mongo DB");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			String source = null;
			String target = null;
			String mongoUri = null;

			if (cmd.hasOption("h")) {
				help();
			}
			if (cmd.hasOption("s")) {
				source = cmd.getOptionValue("s");
			}
			if (cmd.hasOption("t")) {
				target = cmd.getOptionValue("t");
			}
			if (cmd.hasOption("d")) {
				mongoUri = cmd.getOptionValue("d");
			}

			if (source != null && target != null && mongoUri != null) {
				MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new MongoClientURI(mongoUri));
				MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
				CityRepository cityRepository = new MongoRepositoryFactory(mongoTemplate)
						.getRepository(CityRepository.class);
				new BookingComHotelCitiesExporter(new File(source), new File(target), cityRepository).doExport();
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
