package com.tripagor.cli;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.tripagor.cli.exporter.BookingComExporter;
import com.tripagor.hotels.HotelServiceRemoteImpl;

public class BookingComExporterCli {

	private static Options options;

	public static void main(String[] args) {
		java.util.logging.LogManager.getLogManager().reset();
		List<Logger> loggers = Collections.<Logger> list(LogManager.getCurrentLoggers());
		loggers.add(LogManager.getRootLogger());
		for (Logger logger : loggers) {
			logger.setLevel(Level.OFF);
		}
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
				BookingComExporter exporter = new BookingComExporter(
						new HotelServiceRemoteImpl(host, clientId, clientSecret));
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
