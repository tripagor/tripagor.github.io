package com.tripagor.cli;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.tripagor.importer.BookingComUmarkedPlaceFinder;

public class BookingComUmarkedPlaceFinderCli {

	private static Options options;

	public static void main(String[] args) {
		BookingComUmarkedPlaceFinder placeFinder = new BookingComUmarkedPlaceFinder();

		options = new Options();
		options.addOption("s", true, "source file");
		options.addOption("t", true, "target file");
		options.addOption("n", true, "maxium number set to be imported");
		options.addOption("h", false, "target file");

		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("h")) {
				help();
			}

			String source = "";
			String target = "";
			int maxNumberOfImports = 0;

			if (cmd.hasOption("t")) {
				target = cmd.getOptionValue("t");
			} else {
				help();
			}
			if (cmd.hasOption("s")) {
				source = cmd.getOptionValue("s");

			} else {
				help();
			}

			if (cmd.hasOption("n")) {
				maxNumberOfImports = Integer.parseInt(cmd.getOptionValue("n"));
			}

			if (maxNumberOfImports > 0) {
				placeFinder.setMaxImports(maxNumberOfImports);
			}
			System.out.println(source + " > " + target);
			placeFinder.extract(new File(source), new File(target));
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
