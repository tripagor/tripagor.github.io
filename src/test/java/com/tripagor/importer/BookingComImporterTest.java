package com.tripagor.importer;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class BookingComImporterTest {
	
	private BookingComUmarkedPlaceFinder bookingComImporter;
	
	@Before
	public void before() throws Exception{
		bookingComImporter = new BookingComUmarkedPlaceFinder();
	}

	@Test
	public void doIt() throws Exception{
		bookingComImporter.extract(new File("C:/Users/jensl/Downloads/hotels/sdIUp77XtU4UzouBf47DQ_Africa_1.tsv"), new File("target/sdIUp77XtU4UzouBf47DQ_Africa_1_unmarked.json"));
	}
}
