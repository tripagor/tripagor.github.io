package com.tripagor.exporter;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripagor.importer.model.Lodging;

public class LodgingExporter {

	private final Logger logger = LoggerFactory.getLogger(LodgingExporter.class);
	private ObjectMapper mapper;

	public LodgingExporter() {
		mapper = new ObjectMapper();
	}

	public void export(List<Lodging> lines, File exportFile) throws Exception {
		logger.debug("writing to {}...", exportFile.getName());
		mapper.writerWithDefaultPrettyPrinter().writeValue(exportFile, lines);
		logger.debug("writing to {} succeeded.", exportFile.getName());
	}

}
