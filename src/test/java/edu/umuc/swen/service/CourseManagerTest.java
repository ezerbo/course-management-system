package edu.umuc.swen.service;

import static edu.umuc.swen.domain.util.ParsingUtil.parseDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import edu.umuc.swen.domain.Address;
import edu.umuc.swen.domain.Course;
import edu.umuc.swen.domain.HybridCourse;
import edu.umuc.swen.domain.OnlineCourse;
import edu.umuc.swen.domain.Student;
import edu.umuc.swen.domain.Term;
import edu.umuc.swen.error.CourseOutOfRangeException;
import edu.umuc.swen.error.InvalidOperationException;
import edu.umuc.swen.error.InvalidStudentOperationException;

/**
 * @author ezerbo
 *
 */
public class CourseManagerTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@After
	@Before
	public void init() throws IOException {
		if(Files.exists(Paths.get("./test-term-data.txt"))){
			Files.delete(Paths.get("./test-term-data.txt"));
		}
		if(Files.exists(Paths.get("./course-1-schedule.txt"))){
			Files.delete(Paths.get("./course-1-schedule.txt"));
		}
	}

	@Test
	public void saveTermToFile() {
		assertFalse(Files.exists(Paths.get("./test-term-data.txt")));
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		manager.saveTermToFile("./test-term-data.txt");
		assertTrue(Files.exists(Paths.get("./test-term-data.txt")));
	}
	
	@Test
	public void addCourseThrowsInvalidOperationException() {
		expectedException.expect(InvalidOperationException.class);
		expectedException.expectMessage("Cannot add a course that has already started or ended.");
		Course course = new OnlineCourse(2, "https://swen-646.umuc.edu", "MATH101", new Date(System.currentTimeMillis() - 31536000000L),
				new Date(System.currentTimeMillis() - 31536000000L), "T TH", "FL2019", "6:00PM - 6:30PM");
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		manager.addCourse(course);
	}
	
	@Test
	public void addCourseThrowsCourseOutOfRangeException() {
		expectedException.expect(CourseOutOfRangeException.class);
		expectedException.expectMessage("The maximum number of courses per term is 6.");
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		for(int i = 1; i <= 5; i++) { //Course already has 2 students, try to add 5 more
			Course course = new OnlineCourse(2, "https://swen-646.umuc.edu", "MATH101", new Date(System.currentTimeMillis() + 31536000000L),
					new Date(System.currentTimeMillis() + 31536000000L), "T TH", "FL2019", "6:00PM - 6:30PM");
			manager.addCourse(course);
		}
	}
	
	@Test
	public void removeCourse() {
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		assertEquals(2, manager.getLoadedTerm().getCourses().size());
		manager.removeCourse(1);
		assertEquals(1, manager.getLoadedTerm().getCourses().size());
	}
	
	@Test
	public void removeCourseThrowsInvalidOperationException() throws Exception {
		expectedException.expect(InvalidOperationException.class);
		expectedException.expectMessage("Unable to delete course with id : '3' because it has already started");
		CourseManager manager = addExpiredCourse();
		manager.removeCourse(3);
	}
	
	@Test
	public void addStudentToCourse() {
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		Student student = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
		assertEquals(2, manager.getCourse(1).getStudents().size());
		manager.addStudentToCourse(student, 1);
		assertEquals(3, manager.getCourse(1).getStudents().size());
	}
	
	@Test
	public void addStudentToCourseThrowsInvalidStudentOperationException() throws Exception {
		expectedException.expect(InvalidStudentOperationException.class);
		expectedException.expectMessage("Unable to add students to this course, it has already ended on 04/25/2019");
		CourseManager manager = addExpiredCourse();
		Student student = new Student(1, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
		manager.addStudentToCourse(student, 3);
	}
	
	@Test
	public void addStudentToCourseThrowsInvalidStudentOperationException2() throws Exception {
		expectedException.expect(InvalidStudentOperationException.class);
		expectedException.expectMessage("Unable to add students to this course, the maximum number of students (20) has been reached");
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		for(int i = 3; i <= 21; i++) {
			Student student = new Student(i, "Sherlock", "Holmes", 4.0, "sherlock.holmes@bekerstreet.com", getAddress());
			manager.addStudentToCourse(student, 1);
		}
	}
	
	@Test
	public void removeStudenFromCourse() {
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		assertEquals(2, manager.getCourse(1).getStudents().size());
		manager.removeStudentFromCourse(1, 1);
		assertEquals(1, manager.getCourse(1).getStudents().size());
	}
	
	@Test
	public void removeStudenFromCourseThrows() throws Exception {
		expectedException.expect(InvalidStudentOperationException.class);
		expectedException.expectMessage("Unable to remove students, this course has already started on 04/20/2019");
		CourseManager manager = addExpiredCourse();
		manager.removeStudentFromCourse(1, 3);
	}
	
	@Test
	public void calculateAverageGPA() {
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		manager.changeStudentGpa(1, 1, 4.0);
		manager.changeStudentGpa(2, 1, 3.0);
		assertEquals(3.5, manager.calculateCourseAverageGpa(1), 0);
	}
	
	@Test
	public void generateCoursechedule() {
		String course1Schedule = "Days: T TH, Times: 6:00PM - 6:30PM, Location : (url: https://swen-646.umuc.edu, 1019 2E 221-B Baker Street London UK 188000)";
		
		String allCoursesSchedule = "Days: T TH, Times: 6:00PM - 6:30PM, Location : (url: https://swen-646.umuc.edu, 1019 2E 221-B Baker Street London UK 188000)\n" + 
				"Days: T TH, Times: 6:00PM - 6:30PM, Location : https://swen-603.umuc.edu"; 
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		assertEquals(course1Schedule, manager.generateCourseSchedule(1));
		assertEquals(allCoursesSchedule, manager.generateCourseSchedule());
	}
	
	@Test
	public void saveCourseScheduleToFile() {
		assertFalse(Files.exists(Paths.get("./course-1-schedule.txt")));
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		manager.saveCourseScheduleToFile("./course-1-schedule.txt");
		assertTrue(Files.exists(Paths.get("./course-1-schedule.txt")));
	}
	
	@Test
	public void changeStudentGPA() {
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		manager.changeStudentGpa(1, 1, 3.0);
		assertEquals(3.0, manager.getCourse(1).getStudentGPA(1), 0);
		manager.changeStudentGpa(1, 1, 4.0);
		assertEquals(4.0, manager.getCourse(1).getStudentGPA(1), 0);
	}
	
	@Test
	public void getCourse() {
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		Course course = manager.getCourse(1);
		assertNotNull(course);
		assertTrue(course instanceof HybridCourse);
	}
	
	@Test
	public void loadStudentsIntoCourse() {
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		assertEquals(2, manager.getCourse(1).getStudents().size());
		manager.loadStudentsIntoCourse("./src/test/resources/test-data/students.txt", 1);
		assertEquals(4, manager.getCourse(1).getStudents().size());
	}
	
	@Test
	public void loadCourseFromFile() {
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		assertEquals(2, manager.getLoadedTerm().getCourses().size());
		manager.loadCourse("./src/test/resources/test-data/lab-course.txt");
		assertEquals(3, manager.getLoadedTerm().getCourses().size());
	}
	
	private CourseManager addExpiredCourse() throws Exception {
		Course course = new OnlineCourse(3, "https://swen-646.umuc.edu", "MATH101", parseDate("04/20/2019"),
				parseDate("04/25/2019"), "T TH", "FL2019", "6:00PM - 6:30PM"); //Course has already started
		CourseManager manager = new CourseManager("./term-data/term-with-2-courses-and-2-students.txt");
		Term term = manager.getLoadedTerm();
		List<Course> loadedCourses = term.getCourses();
		loadedCourses.add(course); //Force a course that has already started into the list
		Field courses = Term.class.getDeclaredField("courses");
		courses.setAccessible(true);
		courses.set(term, loadedCourses);
		courses.setAccessible(false);
		return manager;
	}
	
	
	private Address getAddress() {
		return new Address("221-B", "Baker Street", "London", "UK", "188000");
	}
	
}
