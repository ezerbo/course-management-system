package edu.umuc.swen.domain;

import static edu.umuc.swen.domain.util.ParsingUtil.parseDate;
import static org.junit.Assert.assertEquals;

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
public class LabCourseTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private String formattedValue = "<labcourse>\n" + 
			"<id>1</id><termcode>FL2019</termcode><name>MATH101</name><startdate>05/20/2019</startdate><enddate>05/20/2019</enddate><meetingdays>T TH</meetingdays><meetingtimes>6:00PM - 6:30PM</meetingtimes>\n" + 
			"<students>\n" + 
			"<student><id>1</id><firstname>Sherlock</firstname><lastname>Holmes</lastname><overallgpa>4.0</overallgpa><emailaddress>sherlock.holmes@bekerstreet.com</emailaddress><mailingaddress><address><buildingnumber>221-B</buildingnumber><street>Baker Street</street><city>London</city><state>UK</state><zipcode>188000</zipcode></address></mailingaddress></student>\n" + 
			"<student><id>2</id><firstname>Sherlock</firstname><lastname>Holmes</lastname><overallgpa>3.0</overallgpa><emailaddress>sherlock.holmes@bekerstreet.com</emailaddress><mailingaddress><address><buildingnumber>221-B</buildingnumber><street>Baker Street</street><city>London</city><state>UK</state><zipcode>188000</zipcode></address></mailingaddress></student>\n" + 
			"</students>\n" + 
			"<gradebook>\n" + 
			"<grade><studentid>1</studentid><gpa>4.0</gpa></grade>\n" + 
			"<grade><studentid>2</studentid><gpa>3.0</gpa></grade>\n" + 
			"</gradebook>\n" + 
			"<classroomlocation>\n" + 
			"<location><roomnumber>2E</roomnumber><buildingname>1019</buildingname><address><buildingnumber>221-B</buildingnumber><street>Baker Street</street><city>London</city><state>UK</state><zipcode>188000</zipcode></address></location>\n" + 
			"</classroomlocation>\n" + 
			"<labroomlocation>\n" + 
			"<location><roomnumber>5E</roomnumber><buildingname>1019</buildingname><address><buildingnumber>221-B</buildingnumber><street>Baker Street</street><city>London</city><state>UK</state><zipcode>188000</zipcode></address></location>\n" + 
			"</labroomlocation>\n" + 
			"</labcourse>";

	@Test
	public void toStringReturnsCorrectFormat() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		Location labRoomLocation = new Location("5E", "1019", address);
		
		LabCourse labCourse = new LabCourse(1, "MATH101", new Date(1558396800000L), new Date(1558396800000L), "T TH",
				"6:00PM - 6:30PM", "FL2019", classroomLocation, labRoomLocation);
		Student student1 = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
		Student student2 = new Student(2, "Sherlock", "Holmes", 3.0, "sherlock.holmes@bekerstreet.com", getAddress());
		labCourse.addStudent(student1);
		labCourse.addStudent(student2);
		labCourse.changeStudentGpa(1, 4.0);
		labCourse.changeStudentGpa(2, 3.0);
		
		System.out.println(labCourse);
		assertEquals(formattedValue , labCourse.toString());
	}
	
	
	@Test
	public void constructorParsesStringValuesCorrectly() {
		LabCourse labCourse = new LabCourse(formattedValue);
		assertEquals(1, labCourse.getId());
		assertEquals("MATH101", labCourse.getName());
		assertEquals("FL2019", labCourse.getTermCode());
		assertEquals("T TH", labCourse.getMeetingDays());
		assertEquals("6:00PM - 6:30PM", labCourse.getMeetingTimes());
		
		Student student = labCourse.getStudents().get(0);
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
		
		Map<Integer, Double> gradebook = labCourse.getGradebook();
		assertEquals(new Double(4.0), gradebook.get(1));
		assertEquals(new Double(3.0), gradebook.get(2));
		
		Location classroomLocation = labCourse.getClassroomLocation();
		assertEquals("2E", classroomLocation.getRoomNumber());
		assertEquals("1019", classroomLocation.getBuildingName());
		assertEquals(address.toString(), classroomLocation.getAddress().toString());
		
		Location labRoomLocation = labCourse.getLabRoomLocation();
		assertEquals("5E", labRoomLocation.getRoomNumber());
		assertEquals("1019", labRoomLocation.getBuildingName());
		assertEquals(address.toString(), labRoomLocation.getAddress().toString());
	}
	
	@Test
	public void generateScheduleReturnsCorrectValue() {
		LabCourse labCourse = getCourse();
		System.out.println(labCourse.generateSchedule());
		assertEquals("Days: T TH, Times: 6:00PM - 6:30PM, Location : (class: 1019 2E 221-B Baker Street London UK 188000,"
				+ " lab: 1019 5E 221-B Baker Street London UK 188000)", labCourse.generateSchedule());
	}
	
	@Test
	public void addStudentThrowsExceptionWhenCourseEnded() {
		expectedException.expect(InvalidStudentOperationException.class);
		expectedException.expectMessage("Unable to add students to this course, it has already ended on 04/23/2019");
		Location classroomLocation = new Location("2E", "1019", getAddress());
		Location labRoomLocation = new Location("5E", "1019", getAddress());
		//Course ended
		Course course = new LabCourse(1, "MATH101", parseDate("03/23/2019"), parseDate("04/23/2019"), "T TH",
				"6:00PM - 6:30PM", "FL2019", classroomLocation, labRoomLocation);
		Student student = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
		course.addStudent(student);
	}
	
	@Test
	public void addStudentThrowsExceptionWhenCourseIsFull() {
		expectedException.expect(InvalidStudentOperationException.class);
		expectedException.expectMessage("Unable to add students to this course, the maximum number of students (10) has been reached");
		Location classroomLocation = new Location("2E", "1019", getAddress());
		Location labRoomLocation = new Location("5E", "1019", getAddress());
		//Course starts and ends in the future
		Course course = new LabCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 2 * 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", classroomLocation, labRoomLocation);
		
		for(int i = 1; i <= 10; i++) { // 10 students in the course
			course.addStudent(new Student(i, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress()));
		}
		
		assertEquals(10, course.getStudents().size());
		
		//Try adding 11 students
		course.addStudent(new Student(11, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress()));
	}
	
	@Test
	public void loadStudent() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location location = new Location("2E", "1019", address);
		Course course = new LabCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 2 * 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", location, location);
		course.loadStudents("./src/test/resources/test-data/students.txt");
		assertEquals(2, course.getStudents().size());
	}
	
	private LabCourse getCourse() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		Location labRoomLocation = new Location("5E", "1019", address);
		return new LabCourse(1, "MATH101", new Date(1558396800000L), new Date(1558396800000L), "T TH",
				"6:00PM - 6:30PM", "FL2019", classroomLocation, labRoomLocation);
	}
	
	private Address getAddress() {
		return new Address("221-B", "Baker Street", "London", "UK", "188000");
	}
}