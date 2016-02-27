package com.tripagor.importer;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class BookingComImporterTest {
	
	private BookingComImporter bookingComImporter;
	
	@Before
	public void before() throws Exception{
		bookingComImporter = new BookingComImporter();
	}

	@Test
	public void doIt() throws Exception{
		bookingComImporter.doImport(new File("src/test/resources/sdIUp77XtU4UzouBf47DQ_Africa_1.tsv"));
	}
}
