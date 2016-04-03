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

		options.addOption("h", false, "this help");

		options.addOption("s", true, "source file");
		options.addOption("f", true, "source folder");

		options.addOption("r", true, "Host Rest Service");
		options.addOption("i", true, "clientId");
		options.addOption("p", true, "client Secret");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			String source = null;
			String directory = null;
			String host = null;
			String clientId = null;
			String clientSecret = null;

			if (cmd.hasOption("h")) {
				help();
			}
			if (cmd.hasOption("s")) {
				source = cmd.getOptionValue("s");
			}
			if (cmd.hasOption("f")) {
				directory = cmd.getOptionValue("f");
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

			if (source == null && directory == null) {
				help();
			}

			if (host != null && clientId != null && clientSecret != null) {
				if (source != null) {
					exporter.extract(new File(source), host, clientId, clientSecret);
				} else if (directory != null) {
					exporter.extract(Paths.get(directory), host, clientId, clientSecret);
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
