package com.tripagor.importer;

import java.io.File;
import java.util.List;

import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.DelimitedParserBuilder;
import org.beanio.builder.StreamBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripagor.importer.model.Accommodation;

public class AccommodationExport {

	private final StreamFactory factory;
	private final Logger logger = LoggerFactory.getLogger(AccommodationExport.class);

	public AccommodationExport() {
		this.factory = StreamFactory.newInstance();
		final DelimitedParserBuilder pb = new DelimitedParserBuilder(';');
		pb.enableEscape('/');
		final StreamBuilder sb = new StreamBuilder("csv").format("delimited").parser(pb);
		sb.addRecord(Accommodation.class);
		factory.define(sb);
	}

	public void export(List<Accommodation> lines, File exportFile) throws Exception {
		if (!exportFile.exists()) {
			exportFile.createNewFile();

			BeanWriter beanWriter = factory.createWriter("csv", exportFile);
			for (Accommodation line : lines) {
				beanWriter.write(line);
			}
			beanWriter.close();
		}
	}

}
