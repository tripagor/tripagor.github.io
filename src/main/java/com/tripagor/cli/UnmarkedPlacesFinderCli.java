package com.tripagor.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.tripagor.cli.exporter.UnmarkedLodgingPlacesFinder;

public class UnmarkedPlacesFinderCli {

	private static Options options;

	public static void main(String[] args) {
		UnmarkedLodgingPlacesFinder exporter = new UnmarkedLodgingPlacesFinder();

		options = new Options();

		options.addOption("h", false, "this help");

		options.addOption("d", true, "mongo uri");
		options.addOption("c", true, "collection");

		options.addOption("u", true, "Url RestService");
		options.addOption("i", true, "clientId");
		options.addOption("p", true, "client Secret");

		options.addOption("n", true, "maxium number set to be exported");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			String mongoUri = null;
			String collection = null;
			int numberOfPlacesToAdd = 0;
			String clientId = null;
			String clientSecret = null;
			String restUri = null;
			String key = null;

			if (cmd.hasOption("h")) {
				help();
			}
			if (cmd.hasOption("d")) {
				mongoUri = cmd.getOptionValue("d");
			}
			if (cmd.hasOption("c")) {
				collection = cmd.getOptionValue("c");
			}
			if (cmd.hasOption("u")) {
				restUri = cmd.getOptionValue("u");
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

			if (key != null) {
				help();
			}
			if (numberOfPlacesToAdd > 0) {
				exporter.setNumberOfPlacesToAdd(numberOfPlacesToAdd);
			}
			if (mongoUri != null && collection != null) {
				exporter.export(mongoUri, collection, key);
			} else if (restUri != null && clientId != null && clientSecret != null) {
				exporter.export(restUri, clientId, clientSecret, key);
			}

			else {
				help();
			}
		} catch (Exception e) {
			System.err.println("An problem occured:" + e);
		}
	}

	private static void help() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp(UnmarkedPlacesFinderCli.class.getName(), options);
		System.exit(0);
	}

}
