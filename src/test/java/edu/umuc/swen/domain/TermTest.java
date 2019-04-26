package edu.umuc.swen.domain;

import static edu.umuc.swen.domain.util.ParsingUtil.parseDate;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.umuc.swen.error.CourseOutOfRangeException;
import edu.umuc.swen.error.InvalidOperationException;

/**
 * @author ezerbo
 *
 */
public class TermTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); 

	@Test
	public void generateScheduleForCourse() {
		Term term = new Term();
		term.addCourse(getCourse());
		assertEquals("", term.generateCourseSchedule(0));
		assertEquals("Days: T TH, Times: 6:00PM - 6:30PM, Location : (url: https://swen-646.umuc.edu, 1019 2E 221-B Baker Street London UK 188000)", term.generateCourseSchedule(1));
	}
	
	@Test
	public void addCourseThrowsExceptionWhenCourseHasStarted() {
		expectedException.expect(InvalidOperationException.class);
		expectedException.expectMessage("Cannot add a course that has already started or ended.");
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		//Past start date
		Course course = new HybridCourse(1, "MATH101", parseDate("03/20/2019"), new Date(System.currentTimeMillis() + 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		new Term().addCourse(course);
	}
	
	@Test
	public void addCourseThrowsExceptionWhenCourseHasEnded() {
		expectedException.expect(InvalidOperationException.class);
		expectedException.expectMessage("Cannot add a course that has already started or ended.");
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		//Past end date
		Course course = new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), parseDate("03/20/2019"), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		new Term().addCourse(course);
	}
	
	@Test
	public void addCourseThrowsExceptionWhenMaxNumberOfCoursesReached() {
		expectedException.expect(CourseOutOfRangeException.class);
		expectedException.expectMessage("The maximum number of courses per term is 6.");
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		Term term = new Term();
		for(int i = 1; i <= 6; i++) {
			Course course = new HybridCourse(i, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 86400000), "T TH",
					"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
			term.addCourse(course);
		}
		assertEquals(6, term.getCourses().size());
		
		//Try adding a 7 courses
		term.addCourse(getCourse());
	}
	
	@Test
	public void addStudentToCourse() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		Term term = new Term();
		Course course = new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		Student student = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
		term.addCourse(course);
		assertEquals(0, term.getCourses().get(0).getStudents().size());
		
		term.addStudentToCourse(student, 0); //No course with ID 0
		assertEquals(0, term.getCourses().get(0).getStudents().size()); //Still 0
		
		term.addStudentToCourse(student, 1);
		assertEquals(1, term.getCourses().get(0).getStudents().size());
	}
	
	@Test
	public void removeStudentFromCourse() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		Term term = new Term();
		Course course = new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		Student student = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
		term.addCourse(course);
		term.addStudentToCourse(student, 1);
		
		term.removeStudentFromCourse(1, 0);
		assertEquals(1, term.getCourses().get(0).getStudents().size());
		
		term.removeStudentFromCourse(1, 1);
		assertEquals(0, term.getCourses().get(0).getStudents().size());
	}
	
	@Test
	public void generateCourseSchedule() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		Term term = new Term();
		HybridCourse hybridCourse = new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		
		OnlineCourse onlineCourse = new OnlineCourse(2, "https://swen-646.umuc.edu", "MATH101", new Date(System.currentTimeMillis() + 86400000),
				new Date(System.currentTimeMillis() + 86400000), "T TH", "FL2019", "6:00PM - 6:30PM");
		
		LabCourse labCourse = new LabCourse(3, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", classroomLocation, classroomLocation);
		
		term.addCourse(hybridCourse);
		term.addCourse(onlineCourse);
		term.addCourse(labCourse);
		System.out.println(term.generateCourseSchedule());
		System.out.println("\n" + term.generateCourseSchedule(3));
		assertEquals("Days: T TH, Times: 6:00PM - 6:30PM, Location : (class: 1019 2E 221-B Baker Street London UK 188000, lab: 1019 2E 221-B Baker Street London UK 188000)", term.generateCourseSchedule(3));
	}
	
	@Test
	public void calculateAverageGpa() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		Term term = new Term();
		Course course = new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		Student student1 = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
		Student student2 = new Student(2, "Sherlock", "Holmes", 3.0, "sherlock.holmes@bekerstreet.com", getAddress());
		term.addCourse(course);
		term.addStudentToCourse(student1, 1);
		term.addStudentToCourse(student2, 1);
		assertEquals(0, term.calculateCourseAverageGpa(1), 0);//No GPA data added
		
		term.changeStudentGpa(1, 1, 4.0);
		term.changeStudentGpa(2, 1, 3.0);
		assertEquals(3.5, term.calculateCourseAverageGpa(1), 0);//No GPA data added
	}
	
	@Test
	public void saveScheduleToFile() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		Term term = new Term();
		HybridCourse hybridCourse = new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		
		OnlineCourse onlineCourse = new OnlineCourse(2, "https://swen-646.umuc.edu", "MATH101", new Date(System.currentTimeMillis() + 86400000),
				new Date(System.currentTimeMillis() + 86400000), "T TH", "FL2019", "6:00PM - 6:30PM");
		
		LabCourse labCourse = new LabCourse(3, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", classroomLocation, classroomLocation);
		
		term.addCourse(hybridCourse);
		term.addCourse(onlineCourse);
		term.addCourse(labCourse);
		
		term.saveCourseScheduleToFile("./test.txt");
	}
	
	@Test
	public void loadHybridCourse() {
		Term term = new Term();
		assertEquals(0, term.getCourses().size());
		term.loadCourse("./src/test/resources/test-data/hybrid-course.txt");
		assertEquals(1, term.getCourses().size());
		assertEquals("https://swen-646.umuc.edu", ((HybridCourse)term.getCourses().get(0)).getUrl());
	}
	
	@Test
	public void loadOnlieCourse() {
		Term term = new Term();
		assertEquals(0, term.getCourses().size());
		term.loadCourse("./src/test/resources/test-data/online-course.txt");
		assertEquals(1, term.getCourses().size());
	}
	
	@Test
	public void loadLabCourse() {
		Term term = new Term();
		assertEquals(0, term.getCourses().size());
		term.loadCourse("./src/test/resources/test-data/lab-course.txt");
		assertEquals(1, term.getCourses().size());
	}
	
	@Test
	public void calculateAverageGpaThrowsException() {
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No course found with id : 0");
		Term term = new Term();
		term.calculateCourseAverageGpa(0);
	}
	
	@Test
	public void removeCourseThrowsException() throws Exception {
		expectedException.expect(InvalidOperationException.class);
		expectedException.expectMessage("Unable to delete course with id : '1' because it has already started");
		Term term = new Term();
		Field courses = Term.class.getDeclaredField("courses");
		courses.setAccessible(true);
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		//Course has started
		Course course = new HybridCourse(1, "MATH101", parseDate("03/20/2019"), new Date(System.currentTimeMillis() + 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		courses.set(term, Arrays.asList(course));
		term.removeCourse(1);
		courses.setAccessible(false);
	}
	
	@Test
	public void removeCourse() throws Exception {
		Term term = new Term();
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		//Course has started
		Course course = new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		term.addCourse(course);
		assertEquals(1, term.getCourses().size());
		term.removeCourse(1);
		assertEquals(0, term.getCourses().size());
	}
	
	@Test
	public void saveTermToFile() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		Term term = new Term();
		HybridCourse hybridCourse = new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 31536000000L), new Date(System.currentTimeMillis() + 31536000000L), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
		
		OnlineCourse onlineCourse = new OnlineCourse(2, "https://swen-646.umuc.edu", "MATH101", new Date(System.currentTimeMillis() + 31536000000L),
				new Date(System.currentTimeMillis() + 31536000000L), "T TH", "FL2019", "6:00PM - 6:30PM");
		
		LabCourse labCourse = new LabCourse(3, "MATH101", new Date(System.currentTimeMillis() + 31536000000L), new Date(System.currentTimeMillis() + 31536000000L), "T TH",
				"6:00PM - 6:30PM", "FL2019", classroomLocation, classroomLocation);
		
		term.addCourse(hybridCourse);
		term.addCourse(onlineCourse);
		term.addCourse(labCourse);
		
		Student student1 = new Student(1, "Sherlock1", "Holmes1", 4.0, "sherlock.holmes@bekerstreet.com1", getAddress());
		Student student2 = new Student(2, "Sherlock2", "Holmes2", 3.9, "sherlock.holmes@bekerstreet.com2", getAddress());
		Student student3 = new Student(3, "Sherlock3", "Holmes3", 3.8, "sherlock.holmes@bekerstreet.com3", getAddress());
		
		term.addStudentToCourse(student1, 1);
		term.changeStudentGpa(1, 1, 4.0);
		term.addStudentToCourse(student2, 2);
		term.changeStudentGpa(2, 2, 3.9);
		term.addStudentToCourse(student3, 3);
		term.changeStudentGpa(3, 3, 3.8);
		term.saveToFile("./term-data.txt");
	}
	
	@Test
	public void loadTerm() {
		Term term = new Term("./src/test/resources/test-data/term-data.txt");
		assertEquals("FL2019", term.getTermCode());
		assertEquals(3, term.getCourses().size());
	}
	
	private HybridCourse getCourse() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		return new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
	}
	
	private Address getAddress() {
		return new Address("221-B", "Baker Street", "London", "UK", "188000");
	}
}
