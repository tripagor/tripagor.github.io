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

import com.mongodb.MongoClientURI;
import com.tripagor.cli.exporter.HotelMarker;
import com.tripagor.cli.service.PlaceAddApiSeleniumImpl;
import com.tripagor.cli.service.PlaceApiImpl;
import com.tripagor.hotels.HotelRepository;
import com.tripagor.hotels.HotelService;
import com.tripagor.hotels.HotelServicePersistenceImpl;
import com.tripagor.hotels.HotelServiceRemoteImpl;

public class HotelMarkerCli {

	private static Options options;

	public static void main(String[] args) {
		CommandLineParser parser = new DefaultParser();

		options = new Options();
		options.addOption("h", false, "this help");

		options.addOption("a", true, "append string");
		options.addOption("n", true, "maxium number of markers to be placed");

		options.addOption("r", true, "Host Rest Service");
		options.addOption("i", true, "clientId");
		options.addOption("p", true, "client Secret");

		options.addOption("d", true, "Uri Mongo DB");

		options.addOption("k", true, "google api key");
		options.addOption("c", true, "google credentials");

		try {
			CommandLine cmd = parser.parse(options, args);

			String host = null;
			String clientId = null;
			String clientSecret = null;
			String key = null;
			String username = null;
			String password = null;
			int numberOfPlacesToAdd = 0;
			String appendStr = "";
			String mongoUri = null;

			if (cmd.hasOption("h")) {
				help();
			}

			if (cmd.hasOption("n")) {
				numberOfPlacesToAdd = Integer.parseInt(cmd.getOptionValue("n"));
			}
			if (cmd.hasOption("a")) {
				appendStr = cmd.getOptionValue("a");
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
			if (cmd.hasOption("k")) {
				key = cmd.getOptionValue("k");
			}
			if (cmd.hasOption("c")) {
				username = cmd.getOptionValue("c").split(":")[0];
				password = cmd.getOptionValue("c").split(":")[1];
			}
			if (cmd.hasOption("d")) {
				mongoUri = cmd.getOptionValue("d");
			}

			if (key == null && (username == null && password == null)) {
				help();
			}

			HotelService hotelService = null;
			if (mongoUri != null) {
				MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(new MongoClientURI(mongoUri));
				MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
				HotelRepository hotelRepository = new MongoRepositoryFactory(mongoTemplate)
						.getRepository(HotelRepository.class);
				hotelService = new HotelServicePersistenceImpl(hotelRepository);
			} else if (host != null && clientId != null && clientSecret != null) {
				hotelService = new HotelServiceRemoteImpl(host, clientId, clientSecret);
			}

			if (hotelService != null && key != null) {
				new HotelMarker(hotelService, new PlaceApiImpl(key)).doMark(numberOfPlacesToAdd, appendStr);
			} else if (hotelService != null && username != null && password != null) {
				new HotelMarker(hotelService, new PlaceAddApiSeleniumImpl(username, password))
						.doMark(numberOfPlacesToAdd, appendStr);
			} else {
				help();
			}
		} catch (Exception e) {
			System.err.println("An problem occured:" + e);
		}
	}

	private static void help() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp(HotelMarkerCli.class.getName(), options);
		System.exit(0);
	}

}
