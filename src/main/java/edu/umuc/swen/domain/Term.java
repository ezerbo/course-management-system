package edu.umuc.swen.domain;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import edu.umuc.swen.error.CourseOutOfRangeException;
import edu.umuc.swen.error.InvalidOperationException;

/**
 * @author ezerbo
 *
 */
public class Term {
	
	private final static int MAX_NUMBER_OF_COURSE = 6; //06 Courses per term.

	private String termCode;
	
	private List<Course> courses = new LinkedList<>();
	
	public Term(String fileName) {
		//TODO parse the XML formatted string
	}
	
	public void saveToFile(String fileName) {
		
	}
	
	/**
	 * @param course
	 * @throws InvalidOperationException
	 * @throws CourseOutOfRangeException
	 */
	public void addCourse(Course course) throws InvalidOperationException, CourseOutOfRangeException {
		if(course.hasStarted() || course.hasEnded())
			throw new InvalidOperationException("Cannot add a course that has already started or ended.");
		if(courses.size() == MAX_NUMBER_OF_COURSE)
			throw new CourseOutOfRangeException("The maximum number of courses per term is 6.");
		courses.add(course);
	}
	
	public void removeCourse(int courseId) {
		
	}
	
	public String generateCourseSchedule() {
		return courses.stream()
				.map(c -> c.generateSchedule())
				.collect(Collectors.joining("\n"));
	}
	
	/**
	 * @param courseId Identifier of the course to generate schedule for.
	 * @return course schedule
	 */
	public String generateCourseSchedule(int courseId) {
		return courses.stream()
				.filter(c -> (c.getId() == courseId))
				.findAny()
				.map(c -> c.generateSchedule())
				.orElse(""); //An empty string is returned when course is not found
	}
	
	public void addStudentToCourse(Student student, int courseId) {
		courses.stream()
		.filter(c -> (c.getId() == courseId)).findAny()
		.ifPresent(course -> {
			course.addStudent(student);
		});
	}
	
	public void removeStudentFromCourse(int studentId, int courseId) {
		courses.stream()
		.filter(c -> (c.getId() == courseId)).findAny()
		.ifPresent(course -> {
			course.removeStudent(studentId);
		});
	}
	
	public double calculateCourseAverageGpa(int courseId) {
		return courses.stream()
				.filter(c -> (c.getId() == courseId))
				.findAny()
				.map(c -> c.calculateAverageGpa())
				.orElseThrow(() -> new RuntimeException(String.format("No course found with id : %s", courseId)));
		//Cannot return 0 because it would be ambiguous. Is the course missing ? Or is it that no GPA data was added ?
	}
	
	public void saveCourseScheduleToFile(String fileName) {
		generateCourseSchedule();//TODO add content to file.
	}
	
	public Course loadCourse(String fileName) {
		return null;
	}
	
	public void changeStudentGpa(int studentId, int courseId, double gpa) {
		courses.stream()
		.filter(c -> (c.getId() == courseId)).findAny()
		.ifPresent(course -> {
			course.changeStudentGpa(studentId, gpa);
		});
	}
	
	public String getTermCode() {
		return termCode;
	}
	
	public List<Course> getCourses() {
		return courses;
	}
}