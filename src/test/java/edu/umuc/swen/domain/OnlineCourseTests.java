package edu.umuc.swen.domain;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Map;

import org.junit.Test;

/**
 * @author ezerbo
 *
 */
public class OnlineCourseTests {

	private String formattedValue = "<onlinecourse>\n" + 
			"<id>1</id><termcode>FL2019</termcode><name>MATH101</name><startdate>04/20/2019</startdate><enddate>05/20/2019</enddate><meetingdays>T TH</meetingdays><meetingtimes>6:00PM - 6:30PM</meetingtimes>\n" + 
			"<students>\n" + 
			"<student><id>1</id><firstname>Sherlock</firstname><lastname>Holmes</lastname><overallgpa>4.0</overallgpa><emailaddress>sherlock.holmes@bekerstreet.com</emailaddress><mailingaddress><address><buildingnumber>221-B</buildingnumber><street>Baker Street</street><city>London</city><state>UK</state><zipcode>188000</zipcode></address></mailingaddress></student>\n" + 
			"<student><id>2</id><firstname>Sherlock</firstname><lastname>Holmes</lastname><overallgpa>3.0</overallgpa><emailaddress>sherlock.holmes@bekerstreet.com</emailaddress><mailingaddress><address><buildingnumber>221-B</buildingnumber><street>Baker Street</street><city>London</city><state>UK</state><zipcode>188000</zipcode></address></mailingaddress></student>\n" + 
			"</students>\n" + 
			"<gradebook>\n" + 
			"<grade><studentid>1</studentid><gpa>4.0</gpa></grade>\n" + 
			"<grade><studentid>2</studentid><gpa>3.0</gpa></grade>\n" + 
			"</gradebook>\n" + 
			"<url>https://swen-646.umuc.edu</url>\n" + 
			"</onlinecourse>";

	@Test
	public void toStringReturnsCorrectFormat() {
		OnlineCourse course = getOnlineCourse();
		Student student1 = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
		Student student2 = new Student(2, "Sherlock", "Holmes", 3.0, "sherlock.holmes@bekerstreet.com", getAddress());
		course.addStudent(student1);
		course.addStudent(student2);
		course.changeStudentGpa(1, 4.0);
		course.changeStudentGpa(2, 3.0);
		assertEquals(formattedValue , course.toString());
	}
	
	@Test
	public void constructorParsesStringValuesCorrectly() {
		OnlineCourse onlineCourse = new OnlineCourse(formattedValue);
		assertEquals(1, onlineCourse.getId());
		assertEquals("https://swen-646.umuc.edu", onlineCourse.getUrl());
		assertEquals("MATH101", onlineCourse.getName());
		assertEquals("FL2019", onlineCourse.getTermCode());
		assertEquals("T TH", onlineCourse.getMeetingDays());
		assertEquals("6:00PM - 6:30PM", onlineCourse.getMeetingTimes());
		
		Student student = onlineCourse.getStudents().get(0);
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
		
		Map<Integer, Double> gradebook = onlineCourse.getGradebook();
		assertEquals(new Double(4.0), gradebook.get(1));
		assertEquals(new Double(3.0), gradebook.get(2));
	}
	
	@Test
	public void generateScheduleReturnsCorrectValue() {
		OnlineCourse onlineCourse = getOnlineCourse();
		System.out.println(onlineCourse.generateSchedule());
		assertEquals("Days: T TH, Times: 6:00PM - 6:30PM, Location : https://swen-646.umuc.edu", onlineCourse.generateSchedule());
	}
	
	private OnlineCourse getOnlineCourse() {
		return new OnlineCourse(1, "https://swen-646.umuc.edu", "MATH101", new Date(1555804800000L),
				new Date(1558396800000L), "T TH", "FL2019", "6:00PM - 6:30PM");
	}
	
	private Address getAddress() {
		return new Address("221-B", "Baker Street", "London", "UK", "188000");
	}
}
