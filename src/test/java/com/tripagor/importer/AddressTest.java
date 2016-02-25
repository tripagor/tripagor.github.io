package com.tripagor.importer;

import com.tripagor.importer.model.Address;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class AddressTest {

	@Test
	public void simpleAddress() throws Exception{
		Address address = new Address("Vogt-Mühlstein-Ring 7, 78052 Villingen-Schwenningen, Deutschland");
		assertThat(address.getCity(), equalTo("78052 Villingen-Schwenningen"));
		assertThat(address.getCountry(), equalTo("Deutschland"));
		assertThat(address.getStreet(), equalTo("Vogt-Mühlstein-Ring 7"));
		assertThat(address.getSuburb(),equalTo(null));
	}
	@Test
	public void detailedAddress() throws Exception{
		Address address = new Address("Vogt-Mühlstein-Ring 7, Stankert, 78052 Villingen-Schwenningen, Deutschland");
		assertThat(address.getCity(), equalTo("78052 Villingen-Schwenningen"));
		assertThat(address.getCountry(), equalTo("Deutschland"));
		assertThat(address.getStreet(), equalTo("Vogt-Mühlstein-Ring 7"));
		assertThat(address.getSuburb(),equalTo("Stankert"));
	}

}
