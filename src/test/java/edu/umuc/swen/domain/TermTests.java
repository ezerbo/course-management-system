package edu.umuc.swen.domain;

import static edu.umuc.swen.domain.util.ParsingUtil.parseDate;
import static org.junit.Assert.assertEquals;

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
public class TermTests {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); 

	@Test
	public void generateScheduleForCourse() {
		Term term = new Term("");
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
		new Term("").addCourse(course);
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
		new Term("").addCourse(course);
	}
	
	@Test
	public void addCourseThrowsExceptionWhenMaxNumberOfCoursesReached() {
		expectedException.expect(CourseOutOfRangeException.class);
		expectedException.expectMessage("The maximum number of courses per term is 6.");
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		Term term = new Term("");
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
	public void generateCourseSchedule() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		Term term = new Term("");
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
	
	private HybridCourse getCourse() {
		Address address = new Address("221-B", "Baker Street", "London", "UK", "188000");
		Location classroomLocation = new Location("2E", "1019", address);
		return new HybridCourse(1, "MATH101", new Date(System.currentTimeMillis() + 86400000), new Date(System.currentTimeMillis() + 86400000), "T TH",
				"6:00PM - 6:30PM", "FL2019", "https://swen-646.umuc.edu", classroomLocation);
	}
}
