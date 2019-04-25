package edu.umuc.swen.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author ezerbo
 *
 */
public class AddressTests {
	
	private String formattedValue = "<address><buildingnumber>221-B</buildingnumber><street>Baker Street</street><city>London</city><state>UK</state><zipcode>188000</zipcode></address>";
	
	@Test
	public void toStringReturnsCorrectFormat() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		assertEquals(formattedValue, address.toString());
		System.out.println(address);
	}
	
	@Test
	public void constructorParsesStringValuesCorrectly() {
		Address address = new Address(formattedValue);
		assertEquals("221-B", address.getBuildingNumber());
		assertEquals("Baker Street", address.getStreet());
		assertEquals("London", address.getCity());
		assertEquals("UK", address.getState());
		assertEquals("188000", address.getZipCode());
	}
}
