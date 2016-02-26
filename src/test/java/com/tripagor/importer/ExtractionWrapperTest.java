package com.tripagor.importer;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
	public void doIt() throws Exception {
		List<Accommodation> accommodations = new LinkedList<Accommodation>();
		List<AffiliateSourceLine> lines = affiliateImport.transform(new File("src/test/resources/selection.csv"));
		for (AffiliateSourceLine line : lines) {
			System.out.println("url=" + line.getUrl());
			try {
				Accommodation accommodation = bookingComAccommodationExtractor.extract(line.getUrl());
				accommodations.add(accommodation);
			} catch (Exception e) {
				System.out.println("error=" + e);
			}
		}
		accommodationExport.export(accommodations, new File("target/selection.json"));
	}

}
