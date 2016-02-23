package com.tripagor.importer;

import java.io.File;
import java.util.List;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.tripagor.importer.model.Accommodation;
import com.tripagor.importer.model.AffiliateSourceLine;

public class ExtractionWrapperTest {
	private AffiliateImport affiliateImport;
	private BookingComAccommodationExtractor bookingComAccommodationExtractor;
	private AccommodationExport accommodationExport;

	@Before
	public void before() throws Exception {
		affiliateImport = new AffiliateImport();
		bookingComAccommodationExtractor = new BookingComAccommodationExtractor();
		accommodationExport = new AccommodationExport();
	}

	@Test
	public void doIt() throws Exception{
		List<Accommodation> accommodations = new LinkedList<Accommodation>();
		List<AffiliateSourceLine> lines = affiliateImport.transform(new File("src/test/resources/short_link.csv"));
		for(AffiliateSourceLine line: lines){
			System.out.println("url="+line.getUrl());
			Accommodation accommodation = bookingComAccommodationExtractor.extract(line.getUrl());
			accommodations.add(accommodation);
		}
		accommodationExport.export(accommodations, new File("target/export.csv"));
	}

}
