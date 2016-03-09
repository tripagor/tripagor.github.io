package com.tripagor.cli;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.tripagor.exporter.PlaceMarker;

public class PlaceMarkerCli {

	private static Options options;

	public static void main(String[] args) {
		PlaceMarker exporter = new PlaceMarker();

		options = new Options();
		options.addOption("d", true, "mongo uri");
		options.addOption("c", true, "collection");
		options.addOption("a", true, "apennd string");
		options.addOption("n", true, "maxium number of markers to be placed");
		options.addOption("h", false, "this help");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("h")) {
				help();
			}

			String uri = "";
			int numberOfPlacesToAdd = 0;

			if (cmd.hasOption("d")) {
				uri = cmd.getOptionValue("d");
			} else {
				help();
			}

			String collection = "";
			if (cmd.hasOption("c")) {
				collection = cmd.getOptionValue("c");
			} else {
				help();
			}

			if (cmd.hasOption("n")) {
				numberOfPlacesToAdd = Integer.parseInt(cmd.getOptionValue("n"));
			}
			String appendStr = null;
			if (cmd.hasOption("a")) {
				appendStr = cmd.getOptionValue("a");
			}

			if (numberOfPlacesToAdd > 0) {
				exporter.setNumberOfPlacesToAdd(numberOfPlacesToAdd);
			}
			if (appendStr != null) {
				exporter.setAppendStr(appendStr);
			}
			System.out.println("Adding places for " + uri);
			exporter.doMark(uri, collection);
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
