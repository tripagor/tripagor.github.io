package com.tripagor.cli;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.tripagor.exporter.UnmarkedLodgingPlacesExporter;

public class UnmarkedPlacesExporterCli {

	private static Options options;

	public static void main(String[] args) {
		UnmarkedLodgingPlacesExporter exporter = new UnmarkedLodgingPlacesExporter();

		options = new Options();
		options.addOption("s", true, "source file");
		options.addOption("n", true, "maxium number set to be exported");
		options.addOption("h", false, "this help");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("h")) {
				help();
			}

			String source = "";
			int numberOfPlacesToAdd = 0;

			if (cmd.hasOption("s")) {
				source = cmd.getOptionValue("s");

			} else {
				help();
			}

			if (cmd.hasOption("n")) {
				numberOfPlacesToAdd = Integer.parseInt(cmd.getOptionValue("n"));
			}

			if (numberOfPlacesToAdd > 0) {
				exporter.setNumberOfPlacesToAdd(numberOfPlacesToAdd);
			}
			System.out.println("Adding places for " + source);
			exporter.export(new File(source));
		} catch (Exception e) {
			System.err.println("An problem occured:" + e);
		}
	}

	private static void help() {
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp(BookingComUmarkedPlaceFinderCli.class.getName(), options);
		System.exit(0);
	}

}
