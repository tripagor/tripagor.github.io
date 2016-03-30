package com.tripagor.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.tripagor.cli.exporter.PlaceMarker;

public class PlaceMarkerCli {

	private static Options options;

	public static void main(String[] args) {
		PlaceMarker exporter = new PlaceMarker();
		CommandLineParser parser = new DefaultParser();

		options = new Options();
		options.addOption("h", false, "this help");

		options.addOption("a", true, "append string");
		options.addOption("n", true, "maxium number of markers to be placed");

		options.addOption("k", true, "google api key");

		options.addOption("d", true, "mongo uri");
		options.addOption("c", true, "collection");

		options.addOption("u", true, "Url RestService");
		options.addOption("i", true, "clientId");
		options.addOption("p", true, "client Secret");

		try {
			CommandLine cmd = parser.parse(options, args);

			String restUri = null;
			String clientId = null;
			String clientSecret = null;
			String mongoUri = null;
			String collection = null;
			String key = null;
			int numberOfPlacesToAdd = 0;
			String appendStr = null;

			if (cmd.hasOption("h")) {
				help();
			}
			if (cmd.hasOption("d")) {
				mongoUri = cmd.getOptionValue("d");
			}
			if (cmd.hasOption("c")) {
				collection = cmd.getOptionValue("c");
			}
			if (cmd.hasOption("n")) {
				numberOfPlacesToAdd = Integer.parseInt(cmd.getOptionValue("n"));
			}
			if (cmd.hasOption("a")) {
				appendStr = cmd.getOptionValue("a");
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

			if (numberOfPlacesToAdd > 0) {
				exporter.setNumberOfPlacesToAdd(numberOfPlacesToAdd);
			}
			if (appendStr != null) {
				exporter.setAppendStr(appendStr);
			}

			if (key == null) {
				help();
			}
			if (mongoUri != null && collection != null) {
				System.out.println("Adding places for " + mongoUri);
				exporter.doMark(mongoUri, collection, key);
			} else if (restUri != null && clientId != null && clientSecret != null) {
				exporter.doMark(restUri, clientId, clientSecret, key);
			} else {
				help();
			}
		} catch (Exception e) {
			System.err.println("An problem occured:" + e);
		}
	}

	private static void help() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp(PlaceMarkerCli.class.getName(), options);
		System.exit(0);
	}

}
