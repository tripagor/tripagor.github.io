package com.tripagor.importer;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripagor.importer.model.Accommodation;

public class AccommodationExport {

	private final Logger logger = LoggerFactory.getLogger(AccommodationExport.class);
	private ObjectMapper mapper;

	public AccommodationExport() {
		mapper = new ObjectMapper();
	}

	public void export(List<Accommodation> lines, File exportFile) throws Exception {
		logger.debug("writing to {}...", exportFile.getName());
		mapper.writerWithDefaultPrettyPrinter().writeValue(exportFile, lines);
		logger.debug("writing to {} succeeded.", exportFile.getName());
	}

}
