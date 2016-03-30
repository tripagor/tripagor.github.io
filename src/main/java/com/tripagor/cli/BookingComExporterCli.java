package com.tripagor.cli;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.tripagor.cli.exporter.BookingComExporter;

public class BookingComExporterCli {

	private static Options options;

	public static void main(String[] args) {
		BookingComExporter exporter = new BookingComExporter();

		options = new Options();
		options.addOption("s", true, "source file");
		options.addOption("f", true, "source folder");
		options.addOption("d", true, "Mongo DB to export uri");
		options.addOption("c", true, "collection name");
		options.addOption("h", false, "this help");

		options.addOption("u", true, "Url RestService");
		options.addOption("i", true, "clientId");
		options.addOption("p", true, "client Secret");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("h")) {
				help();
			}

			String source = null;
			String directory = null;
			String mongoUri = null;
			String collection = null;
			String restUri = null;
			String clientId = null;
			String clientSecret = null;

			if (cmd.hasOption("s")) {
				source = cmd.getOptionValue("s");
			}
			if (cmd.hasOption("f")) {
				directory = cmd.getOptionValue("f");
			}
			if (cmd.hasOption("c")) {
				collection = cmd.getOptionValue("c");
			}
			if (cmd.hasOption("d")) {
				mongoUri = cmd.getOptionValue("d");
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

			if (source == null && directory == null) {
				help();
			}

			if (restUri != null && clientId != null && clientSecret != null) {
				if (source != null) {
					exporter.extract(new File(source), restUri, clientId, clientSecret);
				} else if (directory != null) {
					exporter.extract(Paths.get(directory), restUri, clientId, clientSecret);
				}
			} else if (mongoUri != null && collection != null) {
				if (source != null) {
					exporter.extract(new File(source), mongoUri, collection);
				} else if (directory != null) {
					exporter.extract(Paths.get(directory), mongoUri, collection);
				}
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
