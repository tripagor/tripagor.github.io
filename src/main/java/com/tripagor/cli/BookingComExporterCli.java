package com.tripagor.cli;

import java.io.File;
import java.nio.file.Paths;

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
		options.addOption("f", true, "source folder");
		options.addOption("d", true, "Mongo DB to export uri");
		options.addOption("c", true, "collection name");
		options.addOption("h", false, "this help");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("h")) {
				help();
			}

			String source = "";
			String directory = "";
			String uri = "";
			String collection = "";

			if (cmd.hasOption("s")) {
				source = cmd.getOptionValue("s");
			}
			if (cmd.hasOption("f")) {
				directory = cmd.getOptionValue("f");
			}

			if ("".equals(source) && "".equals(directory)) {
				help();
			}

			if (cmd.hasOption("c")) {
				collection = cmd.getOptionValue("c");

			} else {
				help();
			}
			if (cmd.hasOption("d")) {
				uri = cmd.getOptionValue("d");

			} else {
				help();
			}

			System.out.println("importing " + source + " > " + uri);
			if (!"".equals(source)) {
				exporter.extract(new File(source), uri, collection);
			} else if (!"".equals(directory)) {
				exporter.extract(Paths.get(directory), uri, collection);
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
