package com.tripagor.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.tripagor.cli.exporter.PlaceMarker;
import com.tripagor.cli.service.PlaceApiImpl;
import com.tripagor.cli.service.PlaceAddApiSeleniumImpl;
import com.tripagor.hotels.HotelServiceRemoteImpl;

public class PlaceMarkerCli {

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
			String appendStr = null;

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
				key = cmd.getOptionValue("c");
			}

			if (key != null || (username == null && password == null)) {
				help();
			}

			PlaceMarker exporter = null;
			if (key != null) {
				exporter = new PlaceMarker(new HotelServiceRemoteImpl(host, clientId, clientSecret),
						new PlaceApiImpl(key));
			} else {
				new PlaceMarker(new HotelServiceRemoteImpl(host, clientId, clientSecret),
						new PlaceAddApiSeleniumImpl(username, password));
			}
			if (numberOfPlacesToAdd > 0) {
				exporter.setNumberOfPlacesToAdd(numberOfPlacesToAdd);
			}
			if (appendStr != null) {
				exporter.setAppendStr(appendStr);
			}

			if (host != null && clientId != null && clientSecret != null) {
				exporter.doMark(host, clientId, clientSecret, key);
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
