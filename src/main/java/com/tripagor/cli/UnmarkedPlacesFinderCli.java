package com.tripagor.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.tripagor.cli.exporter.UnmarkedLodgingPlacesFinder;
import com.tripagor.hotels.HotelServiceRemoteImpl;

public class UnmarkedPlacesFinderCli {

	private static Options options;

	public static void main(String[] args) {

		options = new Options();

		options.addOption("h", false, "this help");

		options.addOption("r", true, "Host RestService");
		options.addOption("i", true, "clientId");
		options.addOption("p", true, "client Secret");

		options.addOption("k", true, "Google API key");

		options.addOption("n", true, "maxium number set to be exported");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			int numberOfPlacesToAdd = 0;
			String clientId = null;
			String clientSecret = null;
			String host = null;
			String key = null;

			if (cmd.hasOption("h")) {
				help();
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

			if (key == null) {
				help();
			}

			UnmarkedLodgingPlacesFinder exporter = new UnmarkedLodgingPlacesFinder(
					new HotelServiceRemoteImpl(host, clientId, clientSecret));
			if (numberOfPlacesToAdd > 0) {
				exporter.setNumberOfPlacesToAdd(numberOfPlacesToAdd);
			}
			if (host != null && clientId != null && clientSecret != null) {
				exporter.export(host, clientId, clientSecret, key);
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
