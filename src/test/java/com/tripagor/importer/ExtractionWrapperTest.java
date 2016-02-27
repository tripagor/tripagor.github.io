package com.tripagor.importer;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.tripagor.importer.model.Accommodation;
import com.tripagor.importer.model.AffiliateSourceLine;

public class ExtractionWrapperTest {
	private AffiliateImport affiliateImport;
	private BookingComAccommodationExtractor bookingComAccommodationExtractor;
	private AccommodationExport accommodationExport;
	private ExecutorService executorService = Executors.newFixedThreadPool(8);

	@Before
	public void before() throws Exception {
		affiliateImport = new AffiliateImport();
		bookingComAccommodationExtractor = new BookingComAccommodationExtractor();
		accommodationExport = new AccommodationExport();
	}

	@After
	public void after() throws Exception {
		executorService.shutdown();
	}

	@Test
	public void doIt() throws Exception {
		List<Accommodation> accommodations = new LinkedList<Accommodation>();
		List<AffiliateSourceLine> lines = affiliateImport.transform(new File("src/test/resources/nuremberg2.csv"));
		List<Callable<Accommodation>> tasks = new LinkedList<Callable<Accommodation>>();

		for (final AffiliateSourceLine line : lines) {
			System.out.println("url=" + line.getUrl());
			try {
				Callable<Accommodation> task = new Callable<Accommodation>() {
					public Accommodation call() throws Exception {
						try {
							return bookingComAccommodationExtractor.extract(line.getUrl());
						} catch (Exception e) {
							return null;
						}
					}
				};
				tasks.add(task);
			} catch (Exception e) {
				System.out.println("error=" + e);
			}
		}

		List<Future<Accommodation>> futures = executorService.invokeAll(tasks);
		for (Future<Accommodation> future : futures) {
			accommodations.add(future.get());
		}
		accommodationExport.export(accommodations, new File("target/nuremberg.json"));
	}

}
