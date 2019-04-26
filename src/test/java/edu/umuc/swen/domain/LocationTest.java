package edu.umuc.swen.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author ezerbo
 *
 */
public class LocationTest {

	private String formattedValue = "<location><roomnumber>10</roomnumber><buildingname>Holmes</buildingname>" + 
			"<address><buildingnumber>221-B</buildingnumber><street>Baker Street</street><city>London</city>" + 
			"<state>UK</state><zipcode>188000</zipcode></address></location>";
	
	@Test
	public void toStringReturnsCorrectFormat() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location location = new Location("10", "Holmes", address);
		assertEquals(formattedValue, location.toString());
		System.out.println(location);
	}
	
	@Test
	public void constructorParsesStringValuesCorrectly() {
		Location location = new Location(formattedValue);
		assertEquals("10", location.getRoomNumber());
		assertEquals("Holmes", location.getBuildingName());
		Address address = location.getAddress();
		assertEquals("221-B", address.getBuildingNumber());
		assertEquals("Baker Street", address.getStreet());
		assertEquals("London", address.getCity());
		assertEquals("UK", address.getState());
		assertEquals("188000", address.getZipCode());
	}
	
}
