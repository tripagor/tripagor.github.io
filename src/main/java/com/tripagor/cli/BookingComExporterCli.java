package com.tripagor.cli;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.tripagor.importer.BookingComExporter;

public class BookingComExporterCli {

	private static Options options;

	public static void main(String[] args) {
		BookingComExporter exporter = new BookingComExporter();

		options = new Options();
		options.addOption("s", true, "source file");
		options.addOption("d", true, "Mongo DB to export uri");
		options.addOption("h", false, "this help");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("h")) {
				help();
			}

			String source = "";
			String uri = "";

			if (cmd.hasOption("s")) {
				source = cmd.getOptionValue("s");

			} else {
				help();
			}
			if (cmd.hasOption("d")) {
				uri = cmd.getOptionValue("d");

			} else {
				help();
			}

			System.out.println("importing " + source + " > " + uri);
			exporter.extract(new File(source), uri);
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
