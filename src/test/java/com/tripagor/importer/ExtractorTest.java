package com.tripagor.importer;

import org.junit.Before;
import org.junit.Test;

import com.tripagor.importer.BookingComAccommodationExtractor;

public class ExtractorTest {
	private BookingComAccommodationExtractor extractor;

	@Before
	public void setUp() throws Exception {
		extractor = new BookingComAccommodationExtractor();
	}

	@Test
	public void test() throws Exception {
		String baseUrl = "http://www.booking.com/hotel/de/zur-waldlust.html?aid=946919";
		System.out.println(extractor.extract(baseUrl));
	}

}
