package com.tripagor.cli;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.springframework.cloud.service.document.MongoDbFactoryFactory;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import com.mongodb.Mongo;
import com.mongodb.MongoClientURI;
import com.tripagor.cli.exporter.BookingComExporter;
import com.tripagor.hotels.HotelRepository;
import com.tripagor.hotels.HotelService;
import com.tripagor.hotels.HotelServicePersistenceImpl;
import com.tripagor.hotels.HotelServiceRemoteImpl;

public class BookingComExporterCli {

	private static Options options;

	public static void main(String[] args) {
		options = new Options();

		options.addOption("h", false, "this help");

		options.addOption("s", true, "source file");
		options.addOption("f", true, "source folder");

		options.addOption("r", true, "Host Rest Service");
		options.addOption("i", true, "clientId");
		options.addOption("p", true, "client Secret");

		options.addOption("d", true, "Uri Mongo DB");
		options.addOption("c", true, "collection");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			String source = null;
			String directory = null;
			String host = null;
			String clientId = null;
			String clientSecret = null;
			String mongoUri = null;
			String collection = null;

			if (cmd.hasOption("h")) {
				help();
			}
			if (cmd.hasOption("s")) {
				source = cmd.getOptionValue("s");
			}
			if (cmd.hasOption("f")) {
				directory = cmd.getOptionValue("f");
			}
			if (cmd.hasOption("r")) {
				host = cmd.getOptionValue("r");
			}
			if (cmd.hasOption("i")) {
				clientId = cmd.getOptionValue("i");
			}
			if (cmd.hasOption("p")) {
				clientSecret = cmd.getOptionValue("p");
			}

			if (cmd.hasOption("d")) {
				mongoUri = cmd.getOptionValue("d");
			}
			if (cmd.hasOption("c")) {
				collection = cmd.getOptionValue("c");
			}

			if (source == null && directory == null) {
				help();
			}

			if (host != null && clientId != null && clientSecret != null) {
				BookingComExporter exporter = new BookingComExporter(
						new HotelServiceRemoteImpl(host, clientId, clientSecret));
				if (source != null) {
					exporter.extract(new File(source), host, clientId, clientSecret);
				} else if (directory != null) {
					exporter.extract(Paths.get(directory), host, clientId, clientSecret);
				}
			} else if (mongoUri != null && collection != null) {
				Mongo mongo = new Mongo();
				MongoTemplate mongoTemplate = new MongoTemplate(mongo);
				HotelRepository hotelRepository = new MongoRepositoryFactory(mongoTemplate).getRepository(HotelRepository.class);
				HotelService hotelService = new HotelServicePersistenceImpl(hotelRepository);
				BookingComExporter exporter = new BookingComExporter(hotelService);
			} else {
				help();
			}

		} catch (Exception e) {
			System.err.println("An problem occured:" + e);
		}
	}

	private static void help() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp(BookingComExporterCli.class.getName(), options);
		System.exit(0);
	}

}
