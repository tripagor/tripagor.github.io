package com.tripagor.importer;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.beanio.builder.DelimitedParserBuilder;
import org.beanio.builder.StreamBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tripagor.importer.model.AffiliateSourceLine;

public class AffiliateImport {

	private final StreamFactory factory;
	private final Logger logger = LoggerFactory.getLogger(AffiliateImport.class);

	public AffiliateImport() {
		this.factory = StreamFactory.newInstance();
		final DelimitedParserBuilder pb = new DelimitedParserBuilder(',');
		pb.enableEscape('/');
		final StreamBuilder sb = new StreamBuilder("csv").format("delimited").parser(pb);
		sb.addRecord(AffiliateSourceLine.class);
		factory.define(sb);
	}

	public List<AffiliateSourceLine> transform(final File file) {
		final List<AffiliateSourceLine> lines = new LinkedList<AffiliateSourceLine>();
		final BeanReader beanReader = this.factory.createReader("csv", file);
		try {
			Object bean;
			beanReader.read();
			while ((bean = beanReader.read()) != null) {
				AffiliateSourceLine line = (AffiliateSourceLine) bean;
				lines.add(line);
			}
		} catch (final Exception e) {
			logger.error("Mapping file {} failed with {}", file.getName(), e);
		} finally {
			beanReader.close();
		}

		return lines;
	}
}
