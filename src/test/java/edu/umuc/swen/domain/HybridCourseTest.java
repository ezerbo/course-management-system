package edu.umuc.swen.domain;

import static org.junit.Assert.assertEquals;
import static edu.umuc.swen.domain.util.ParsingUtil.parseDate;

import java.util.Date;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.umuc.swen.error.InvalidStudentOperationException;

/**
 * @author ezerbo
 *
 */
public class HybridCourseTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private String formattedValue = "<hybridcourse>\n" + 
			"<id>1</id><termcode>FL2019</termcode><name>MATH101</name><startdate>05/20/2019</startdate><enddate>05/20/2019</enddate><meetingdays>T TH</meetingdays><meetingtimes>6:00PM - 6:30PM</meetingtimes>\n" + 
			"<students>\n" + 
			"<student><id>1</id><firstname>Sherlock</firstname><lastname>Holmes</lastname><overallgpa>4.0</overallgpa><emailaddress>sherlock.holmes@bekerstreet.com</emailaddress><mailingaddress><address><buildingnumber>221-B</buildingnumber><street>Baker Street</street><city>London</city><state>UK</state><zipcode>188000</zipcode></address></mailingaddress></student>\n" + 
			"<student><id>2</id><firstname>Sherlock</firstname><lastname>Holmes</lastname><overallgpa>3.0</overallgpa><emailaddress>sherlock.holmes@bekerstreet.com</emailaddress><mailingaddress><address><buildingnumber>221-B</buildingnumber><street>Baker Street</street><city>London</city><state>UK</state><zipcode>188000</zipcode></address></mailingaddress></student>\n" + 
			"</students>\n" + 
			"<gradebook>\n" + 
			"<grade><studentid>1</studentid><gpa>4.0</gpa></grade>\n" + 
			"<grade><studentid>2</studentid><gpa>3.0</gpa></grade>\n" + 
			"</gradebook>\n" + 
			"<url>https://swen-646.umuc.edu</url>\n" + 
			"<classroomlocation>\n" + 
			"<location><roomnumber>2E</roomnumber><buildingname>1019</buildingname><address><buildingnumber>221-B</buildingnumber><street>Baker Street</street><city>London</city><state>UK</state><zipcode>188000</zipcode></address></location>\n" + 
			"</classroomlocation>\n" + 
			"</hybridcourse>";

	@Test
	public void toStringReturnsCorrectFormat() {
		HybridCourse hybridCourse = getCourse();
		Student student1 = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
		Student student2 = new Student(2, "Sherlock", "Holmes", 3.0, "sherlock.holmes@bekerstreet.com", getAddress());
		hybridCourse.addStudent(student1);
		hybridCourse.addStudent(student2);
		hybridCourse.changeStudentGpa(1, 4.0);
		hybridCourse.changeStudentGpa(2, 3.0);
		
		System.out.println(hybridCourse);
		assertEquals(formattedValue , hybridCourse.toString());
	}
	
	@Test
	public void constructorParsesStringValuesCorrectly() {
		HybridCourse hybridCourse = new HybridCourse(formattedValue);
		assertEquals(1, hybridCourse.getId());
		assertEquals("https://swen-646.umuc.edu", hybridCourse.getUrl());
		assertEquals("MATH101", hybridCourse.getName());
		assertEquals("FL2019", hybridCourse.getTermCode());
		assertEquals("T TH", hybridCourse.getMeetingDays());
		assertEquals("6:00PM - 6:30PM", hybridCourse.getMeetingTimes());
		
		Student student = hybridCourse.getStudents().get(0);
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
		
		Map<Integer, Double> gradebook = hybridCourse.getGradebook();
		assertEquals(new Double(4.0), gradebook.get(1));
		assertEquals(new Double(3.0), gradebook.get(2));
		
		Location location = hybridCourse.getClassroomLocation();
		assertEquals("2E", location.getRoomNumber());
		assertEquals("1019", location.getBuildingName());
		assertEquals(address.toString(), location.getAddress().toString());
	}
	
	@Test
	public void generateScheduleReturnsCorrectValue() {
		HybridCourse hybridCourse = getCourse();
		System.out.println(hybridCourse.generateSchedule());
		assertEquals("Days: T TH, Times: 6:00PM - 6:30PM, Location : "
				+ "(url: https://swen-646.umuc.edu, 1019 2E 221-B Baker Street London UK 188000)", hybridCourse.generateSchedule());
	}
	
	@Test
	public void calculateAverageGpa() {
		HybridCourse hybridCourse = getCourse();
		assertEquals(0, hybridCourse.calculateAverageGpa(), 0);
		Student student1 = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
		Student student2 = new Student(2, "Sherlock2", "Holmes2", 3.0, "sherlock.holmes@bekerstreet.com2", getAddress());
		hybridCourse.addStudent(student1);
		hybridCourse.addStudent(student2);
		hybridCourse.changeStudentGpa(1, 4D);
		hybridCourse.changeStudentGpa(2, 3D);
		assertEquals(3.5, hybridCourse.calculateAverageGpa(), 0);
	}
	
	@Test
	public void changeStudentGpaThrowsException() {
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No student found with id: 10");
		HybridCourse hybridCourse = getCourse();
		hybridCourse.changeStudentGpa(10, 4D);
	}
	
	@Test
	public void deleteStudentThrowsException() {
		expectedException.expect(InvalidStudentOperationException.class);
		expectedException.expectMessage("Unable to remove students, this course has already started on 04/23/2019");
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		//Course has already started
		Course course = new HybridCourse(1, "MATH101", parseDate("04/23/2019"), new Date(1558396800000L), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		course.removeStudent(10);
	}
	
	@Test
	public void deleteStudentRemovesStudent() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		//Course starts and ends in the future
		Course course = new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 2 * 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		Student student = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
		course.addStudent(student);
		assertEquals(1, course.getStudents().size());
		
		//Remove missing student, size should not be affected  
		course.removeStudent(0);
		assertEquals(1, course.getStudents().size());
		
		//Remove existing student, size should be 0
		course.removeStudent(1);
		assertEquals(0, course.getStudents().size());
	}
	
	@Test
	public void addStudentThrowsExceptionWhenCourseEnded() {
		expectedException.expect(InvalidStudentOperationException.class);
		expectedException.expectMessage("Unable to add students to this course, it has already ended on 04/23/2019");
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		//Course ended
		Course course = new HybridCourse(1, "MATH101", parseDate("03/23/2019"), parseDate("04/23/2019"), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		Student student = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
		course.addStudent(student);
	}
	
	@Test
	public void addStudentThrowsExceptionWhenCourseIsFull() {
		expectedException.expect(InvalidStudentOperationException.class);
		expectedException.expectMessage("Unable to add students to this course, the maximum number of students (20) has been reached");
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		
		//Course starts and ends in the future
		Course course = new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 2 * 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		
		for(int i = 1; i <= 20; i++) { // 20 students in the course
			course.addStudent(new Student(i, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress()));
		}
		
		assertEquals(20, course.getStudents().size());
		
		//Try adding 21 students
		course.addStudent(new Student(21, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress()));
	}
	
	@Test
	public void loadStudent() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		Course course = new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 2 * 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		course.loadStudents("./src/test/resources/test-data/students.txt");
		assertEquals(2, course.getStudents().size());
	}
	
	private HybridCourse getCourse() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		return new HybridCourse(1, "MATH101", new Date(1558396800000L), new Date(1558396800000L), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
	}
	
	private Address getAddress() {
		return new Address("221-B", "Baker Street", "London", "UK", "188000");
	}
	
}