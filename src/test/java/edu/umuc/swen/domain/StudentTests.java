package edu.umuc.swen.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author ezerbo
 *
 */
public class StudentTests {
	
	private String formattedValue = "<student><id>1</id><firstname>Sherlock</firstname>" + 
			"<lastname>Holmes</lastname><overallgpa>4.0</overallgpa><emailaddress>sherlock.holmes@bekerstreet.com</emailaddress><mailingaddress>" + 
			"<address><buildingnumber>221-B</buildingnumber><street>Baker Street</street><city>London</city>" + 
			"<state>UK</state><zipcode>188000</zipcode></address></mailingaddress></student>";
	
	@Test
	public void toStringReturnsCorrectFormat() {
		Address mailingAddress = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Student student = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", mailingAddress);
		System.out.println(student);
		assertEquals(formattedValue, student.toString());
	}
	
	@Test
	public void constructorParsesStringValuesCorrectly() {
		Student student = new Student(formattedValue);
		assertEquals(1, student.getId());
		assertEquals("Sherlock", student.getFirstName());
		assertEquals("Holmes", student.getLastName());
		assertEquals(4.0, student.getOverallGpa(), 0);
		assertEquals("sherlock.holmes@bekerstreet.com", student.getEmailAddress());
		
		Address address = student.getMailingAddress();
		assertEquals("221-B", address.getBuildingNumber());
		assertEquals("Baker Street", address.getStreet());
		assertEquals("London", address.getCity());
		assertEquals("UK", address.getState());
		assertEquals("188000", address.getZipCode());
	}
	
}
