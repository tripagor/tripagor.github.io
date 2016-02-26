package com.tripagor.importer;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.tripagor.importer.model.AffiliateSourceLine;

public class AffiliateImporterTest {
	private AffiliateImport affiliateImport;

	@Before
	public void before() throws Exception {
		affiliateImport = new AffiliateImport();
	}

	@Test
	public void test() throws Exception {
		List<AffiliateSourceLine> lines = affiliateImport.transform(new File("src/test/resources/links.csv"));
		System.out.println("lines="+lines);
	}
}
