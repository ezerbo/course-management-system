package edu.umuc.swen.domain;

import static edu.umuc.swen.domain.util.FileUtil.loadFromFile;
import static edu.umuc.swen.domain.util.FileUtil.writeToFile;
import static edu.umuc.swen.domain.util.ParsingUtil.getPropertyValue;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.umuc.swen.error.CourseOutOfRangeException;
import edu.umuc.swen.error.InvalidOperationException;

/**
 * Domain class modeling a term
 * 
 * @author ezerbo
 *
 */
public class Term {
	
	private final static int MAX_NUMBER_OF_COURSE = 6; //06 Courses per term.

	private String termCode;
	
	private List<Course> courses = new LinkedList<>();
	
	public Term(String fileName) {
		String content = loadFromFile(fileName);
		this.termCode = getPropertyValue(content, "termcode");
		this.courses = parseCourses(getPropertyValue(content, "courses"));
	}
	
	public Term() {
	}
	
	/**
	 * Saves Term data to a file
	 * 
	 * @param fileName File to save term data to.
	 */
	public void saveToFile(String fileName) {
		writeToFile(fileName, toString());
	}
	
	/**
	 * @param course
	 * @throws InvalidOperationException
	 * @throws CourseOutOfRangeException
	 */
	public void addCourse(Course course) throws InvalidOperationException, CourseOutOfRangeException {
		if(course.hasStarted() || course.hasEnded())
			throw new InvalidOperationException("Cannot add a course that has already started or ended.");
		if(isTermFull())
			throw new CourseOutOfRangeException("The maximum number of courses per term is 6.");
		courses.add(course);
	}
	
	/**
	 * Removes a course from this term
	 * 
	 * @param courseId Identifier of the course to remove
	 */
	public void removeCourse(int courseId) {
		courses.stream()
		.filter(c -> (c.getId() == courseId)).findAny()
		.ifPresent(course -> {
			if(course.hasStarted())
				throw new InvalidOperationException(
						String.format("Unable to delete course with id : '%s' because it has already started", courseId));
			courses.remove(course);
		});
	}
	
	/**
	 * Generates the schedule for all courses in this term
	 * 
	 * @return schedules
	 */
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
	
	/**
	 * Adds a student to a course
	 * 
	 * @param student Student to be added
	 * @param courseId Identifier of the course to add the student to.
	 */
	public void addStudentToCourse(Student student, int courseId) {
		courses.stream()
		.filter(c -> (c.getId() == courseId)).findAny()
		.ifPresent(course -> {
			course.addStudent(student);
		});
	}
	
	/**
	 * Removes a student from a course
	 * 
	 * @param studentId Identifier of the student
	 * @param courseId Indentifier of the course
	 */
	public void removeStudentFromCourse(int studentId, int courseId) {
		courses.stream()
		.filter(c -> (c.getId() == courseId)).findAny()
		.ifPresent(course -> {
			course.removeStudent(studentId);
		});
	}
	
	/**
	 * Calculates the average GPA of a course
	 * 
	 * @param courseId Identifier  of the course
	 * @return averageGpa
	 */
	public double calculateCourseAverageGpa(int courseId) {
		return courses.stream()
				.filter(c -> (c.getId() == courseId))
				.findAny()
				.map(c -> c.calculateAverageGpa())
				.orElseThrow(() -> new RuntimeException(String.format("No course found with id : %s", courseId)));
		//Cannot return 0 because it would be ambiguous. Is the course missing ? Or is it that no GPA data was added ?
	}
	
	/**
	 * Saves course schedule to a file.
	 * 
	 * @param fileName File to save course schedule to.
	 */
	public void saveCourseScheduleToFile(String fileName) {
		writeToFile(fileName, generateCourseSchedule());
	}
	
	/**
	 * Loads a course into the term
	 * 
	 * @param fileName File to load the course from
	 */
	public void loadCourse(String fileName) {
		if(isTermFull())
			throw new CourseOutOfRangeException("The maximum number of courses per term is 6.");
		String content = loadFromFile(fileName);
		Course course = getCourseFromString(content);
		if(course.hasStarted() || course.hasEnded())
			throw new InvalidOperationException("Cannot add a course that has already started or ended.");
		courses.add(course);
	}
	
	/**
	 * Creates a new course based on the starting tag of 'courseLine'
	 * 
	 * @param courseLine Content to create a course from.
	 * @return The new Course
	 */
	private Course getCourseFromString(String courseLine) {
		if(courseLine.startsWith("<onlinecourse>")) {
			return new OnlineCourse(courseLine);
		} else if (courseLine.startsWith("<hybridcourse>")) {
			return new HybridCourse(courseLine);
		} else if (courseLine.startsWith("<labcourse>")) {
			return new LabCourse(courseLine);
		}
		throw new RuntimeException("No such course type");//
	}
	
	/**
	 * Updates a student's GPA
	 * 
	 * @param studentId Identifier of student
	 * @param courseId Identifier of the course
	 * @param gpa New GPA
	 */
	public void changeStudentGpa(Integer studentId, int courseId, Double gpa) {
		Course course = courses.stream()
				.filter(c -> (c.getId() == courseId)).findAny()
				.orElseThrow(() -> new RuntimeException(""));
		course.changeStudentGpa(studentId, gpa);
	}
	
	/** 
	 * Returns the code of this term
	 * 
	 * @return termCode
	 */
	public String getTermCode() {
		return termCode;
	}
	
	/**
	 * Returns this term's courses
	 * 
	 * @return courses
	 */
	public List<Course> getCourses() {
		return courses;
	}
	
	
	/**
	 * Test whether this term is full (if it contains 6 courses) 
	 * 
	 * @return indicator
	 */
	private boolean isTermFull() {
		return courses.size() == MAX_NUMBER_OF_COURSE;
	}
	
	/**
	 * Joins the string representation of the courses, delimited by the new lines
	 * 
	 * @return String representation of all courses
	 */
	private String formatCourses() {
		return courses.stream()
				.map(course -> course.toString())
				.collect(Collectors.joining("\n"));
	}
	
	/**
	 * Parses courses for this term
	 * 
	 * @param courseLines String representation of courses
	 * @return A list of courses
	 * @throws CourseOutOfRangeException When a minimum of one course is not found
	 */
	private List<Course> parseCourses(String courseLines) throws CourseOutOfRangeException {
		if(Objects.isNull(courseLines)) throw new CourseOutOfRangeException("A minimum of one course is required");
		List<Course> courses = new LinkedList<>();
		while(!(courseLines.isEmpty() || courseLines.equals("\n"))) {
			int closingNodeIndex = 0;
			String startingNode = courseLines.substring(1, courseLines.indexOf("\n", 1));
			if("<hybridcourse>".equals(startingNode)) { 
				closingNodeIndex = courseLines.indexOf("</hybridcourse>\n");
				String courseLine = courseLines.substring(16, closingNodeIndex);
				courses.add(new HybridCourse(courseLine));
				courseLines = courseLines.substring(closingNodeIndex + 15);
			} else if("<onlinecourse>".equals(startingNode)) {
				closingNodeIndex = courseLines.indexOf("</onlinecourse>");
				String courseLine = courseLines.substring(16, closingNodeIndex);
				courses.add(new OnlineCourse(courseLine));
				courseLines = courseLines.substring(closingNodeIndex + 15);
			} else if ("<labcourse>".equals(startingNode)) {
				closingNodeIndex = courseLines.indexOf("</labcourse>");
				String courseLine = courseLines.substring(13, closingNodeIndex);
				courses.add(new LabCourse(courseLine));
				courseLines = courseLines.substring(closingNodeIndex + 12);
			}
		}
		return courses;
	}
	
	/**
	 * Finds a course using its ID
	 * 
	 * @param courseId course identifier
	 * @return the course
	 */
	public Course getCourse(int courseId) {
		return courses.stream()
				.filter(c -> c.getId() == courseId)
				.findFirst()
				.orElseThrow(() -> new RuntimeException(String.format("No course found with id: %s", courseId)));
	}
	
	/**
	 * Loads students from a file into a course
	 * 
	 * @param fileName File to load students from
	 * @param courseId Identifier of course to load students into
	 */
	public void loadStudentsIntoCourse(String fileName, int courseId) {
		courses.stream()
		.filter(c -> c.getId() == courseId)
		.findFirst()
		.ifPresent(course -> {
			course.loadStudents(fileName);
		});
	}
	
	public String toString() {
		return new StringBuilder()
				.append("<term>\n")
				.append("<termcode>")
				.append(termCode)
				.append("</termcode>\n")
				.append("<courses>\n")
				.append(formatCourses())
				.append("\n</courses>\n")
				.append("</term>")
				.toString();
	}
}