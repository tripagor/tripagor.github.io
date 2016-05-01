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
import com.tripagor.cli.exporter.HotelMarker;
import com.tripagor.cli.service.PlaceApiImpl;
import com.tripagor.hotels.HotelRepository;
import com.tripagor.hotels.HotelService;
import com.tripagor.hotels.HotelServicePersistenceImpl;
import com.tripagor.hotels.HotelServiceRemoteImpl;

public class HotelMarkerCli {

	private static Options options;

	public static void main(String[] args) {

		options = new Options();

		options.addOption("h", false, "this help");

		options.addOption("r", true, "Host RestService");
		options.addOption("i", true, "clientId");
		options.addOption("p", true, "client Secret");

		options.addOption("d", true, "Uri Mongo DB");

		options.addOption("k", true, "Google API key");

		options.addOption("n", true, "maxium number mark");

		options.addOption("a", true, "url append string");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			int numberOfPlacesToAdd = 1;
			String clientId = null;
			String clientSecret = null;
			String host = null;
			String key = null;
			String mongoUri = null;
			String urlAppendStr = null;

			if (cmd.hasOption("h")) {
				help();
			}
			if (cmd.hasOption("a")) {
				urlAppendStr = cmd.getOptionValue("a");
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
			if (cmd.hasOption("n")) {
				numberOfPlacesToAdd = Integer.parseInt(cmd.getOptionValue("n"));
			}
			if (cmd.hasOption("d")) {
				mongoUri = cmd.getOptionValue("d");
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
				new HotelMarker(hotelService, new PlaceApiImpl(key), new GeoApiContext().setApiKey(key))
						.doHandle(numberOfPlacesToAdd, urlAppendStr);
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
