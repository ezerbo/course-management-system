/**
 * 
 */
package edu.umuc.swen.domain;

import static edu.umuc.swen.domain.util.FileUtil.loadFromFile;
import static edu.umuc.swen.domain.util.ParsingUtil.format;
import static edu.umuc.swen.domain.util.ParsingUtil.getPropertyValue;
import static edu.umuc.swen.domain.util.ParsingUtil.parseDate;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import edu.umuc.swen.error.InvalidStudentOperationException;

/**
 * Domain class modeling a course
 * 
 * @author ezerbo
 *
 */
public abstract class Course {
	
	/**
	 *  Maximum number of students that can be added to course, set to 20
	 */
	private final static int MAX_NUMBER_OF_STUDENTS = 20;
	
	/**
	 * Course identifier
	 */
	protected int id;
	
	/**
	 * Name of the course
	 */
	protected String name;
	
	/**
	 * Start date
	 */
	protected Date startDate;
	
	/**
	 * End date
	 */
	protected Date endDate;
	
	/**
	 * Meeting days, example: "T TH" for Tuesdays and Thursdays
	 */
	protected String meetingDays;
	
	/**
	 * Meeting times, example: "6:00 PM - 7:00PM"
	 */
	protected String meetingTimes;
	
	/**
	 * List of students
	 */
	protected List<Student> students = new LinkedList<>();
	
	/**
	 * Gradebook associated to the course
	 */
	protected Map<Integer, Double> gradebook = new HashMap<>();
	
	/**
	 * Code of the term a course is added to
	 */
	protected String termCode;
	
	/**
	 * Creates an instance of Course using its string representation
	 * 
	 * @param line String to parse course data from
	 */
	public Course (String line) {
		this.id = parseInt(getPropertyValue(line, "id"));
		this.name = getPropertyValue(line, "name");
		this.startDate = parseDate(getPropertyValue(line, "startdate"));
		this.endDate = parseDate(getPropertyValue(line, "enddate"));
		this.meetingDays = getPropertyValue(line, "meetingdays");
		this.meetingTimes = getPropertyValue(line, "meetingtimes");
		this.termCode = getPropertyValue(line, "termcode");
		this.students = parseStudents(getPropertyValue(line, "students"));
		this.gradebook = parseGradebook(getPropertyValue(line, "gradebook"));
	}
	
	public Course(int id, String name, Date startDate, Date endDate,
			String meetingDays, String meetingTimes, String termCode) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
		this.meetingDays = meetingDays;
		this.meetingTimes = meetingTimes;
		this.termCode = termCode;
	}
	
	/**
	 * Calculates and returns the average GPA for this course
	 * 
	 * @return Average GPA
	 */
	public double calculateAverageGpa() {
		return gradebook.entrySet()
				.stream()
				.mapToDouble(e -> e.getValue())
				.average()
				.orElse(0);// Returns 0 when no entry is found in the gradebook
		
	}
	
	/**
	 * Generate the schedule (example: Days: T TH, Times: 6:00PM - 6:30PM)
	 * 
	 * @return courseSchedule
	 */
	public String generateSchedule() {
		return new StringBuilder()
				.append("Days: " + meetingDays)
				.append(", ")
				.append("Times: " + meetingTimes)
				.toString();
	}
	
	/**
	 * Add a new student to this course
	 * 
	 * @param student Student to be added
	 * @throws InvalidStudentOperationException When course has ended
	 */
	public void addStudent(Student student) throws InvalidStudentOperationException {
		if(hasEnded()) 
			throw new InvalidStudentOperationException(
					String.format("Unable to add students to this course, it has already ended on %s", format(endDate)));
		
		if(isCourseFull())
			throw new InvalidStudentOperationException(
					String.format("Unable to add students to this course, the maximum number of students (20) has been reached", format(endDate)));
		
		students.add(student);
	}
	
	/**
	 * Updates a student's GPA
	 * 
	 * @param studentId Identifier of the student (example: 1)
	 * @param gpa New GPA (example: 4.0)
	 */
	public void changeStudentGpa(Integer studentId, Double gpa) {
		if(students.stream().anyMatch(s -> (s.getId() == studentId))) { //If student exists
			gradebook.put(studentId, gpa);
		} else {
			//TODO replace with StudentNotFoundException
			throw new RuntimeException(String.format("No student found with id: %s", studentId));
		}
	}
	
	/**
	 * Removes a student from a course
	 * 
	 * @param studentId Identifier of the student to be removed
	 * @throws InvalidStudentOperationException When course has already started
	 */
	public void removeStudent(int studentId) throws InvalidStudentOperationException {
		if(hasStarted()) {
			throw new InvalidStudentOperationException(
					String.format("Unable to remove students, this course has already started on %s", format(startDate)));
		}
		students.removeIf(e -> (e.getId() == studentId));
	}
	
	/**
	 * Loads students from a file
	 * 
	 * @param fileName File to load students from
	 */
	public void loadStudents(String fileName) {
		if(hasEnded()) {
			throw new InvalidStudentOperationException(
					String.format("Unable to add students to this course, it has already ended on %s", format(endDate)));
		}
		
		if(isCourseFull()) {
			throw new InvalidStudentOperationException("Unable to add students to this course, the maximum number of students (20) has been reached");
		}
		
		String studentLines = getPropertyValue(loadFromFile(fileName), "students");
		List<Student> laodedStudents = parseStudents(studentLines);
		if(laodedStudents.size() + students.size() > MAX_NUMBER_OF_STUDENTS) {
			throw new InvalidStudentOperationException("Unable to add students to this course, the maximum number of students (20) has been reached");
		}
		students.addAll(laodedStudents);
	}
	
	/**
	 * Test whether a course is full (maximum number of students reached)
	 * 
	 * @return isCourseFull
	 */
	private boolean isCourseFull() {
		return students.size() == MAX_NUMBER_OF_STUDENTS;
	}

	/**
	 * Parses student records, separated by new lines, from a string.
	 * It returns an empty list when no student records are found
	 * Otherwise, it splits the string passed as parameter and for each student record,
	 * it creates an instance of Student.
	 * It then collects all the records into a list and returns it.
	 * 
	 * 
	 * @param students String to parse student records from
	 * @return students A list of students
	 */
	protected List<Student> parseStudents(String students) {
		if(Objects.isNull(students)) return new LinkedList<>(); // When the course is missing students
		return Arrays.stream(students.split("\n")).filter(e -> !e.isEmpty())
				.map(studentLine -> new Student(studentLine))
				.collect(Collectors.toList());
	}
	
	/**
	 * Parses gradebook records, separated by new lines, from a string
	 * 
	 * @param gradebook string to parse gradebook records from
	 * @return gradebook The Gradebook
	 */
	private Map<Integer, Double> parseGradebook(String gradebook) {
		if(Objects.isNull(gradebook)) return new HashMap<>(); // When the course is missing students
		Map<Integer, Double> grades = new HashMap<>();
		Arrays.stream(gradebook.split("\n")).filter(e -> !e.isEmpty())
		.forEach(gradebookEntry -> {
			Integer studentId = parseInt(getPropertyValue(gradebookEntry, "studentid"));
			Double gpa = parseDouble(getPropertyValue(gradebookEntry, "gpa"));
			grades.put(studentId, gpa);
		});
		return grades;
	}
	
	/**
	 * Finds a student's GPA using their identifier
	 * 
	 * @param studentId Identifier of student
	 * @return student's GPA
	 */
	public Double getStudentGPA(Integer studentId) {
		return gradebook.get(studentId);
		
	}
	
	/**
	 * Test whether a course has started
	 * 
	 * @return indicator 
	 */
	protected boolean hasStarted() {
		return new Date().after(startDate);
	}
	
	/**
	 * Test whether a course has ended
	 * 
	 * @return indicator
	 */
	protected boolean hasEnded() {
		return new Date().after(endDate);
	}
	
	/**
	 * @return the termCode The term code
	 */
	public String getTermCode() {
		return termCode;
	}

	/**
	 * @param termCode the termCode to set
	 */
	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @return the meetingDays
	 */
	public String getMeetingDays() {
		return meetingDays;
	}

	/**
	 * @return the meetingTimes
	 */
	public String getMeetingTimes() {
		return meetingTimes;
	}

	/**
	 * @return the students
	 */
	public List<Student> getStudents() {
		return students;
	}

	/**
	 * @return the gradebook
	 */
	public Map<Integer, Double> getGradebook() {
		return new HashMap<>(gradebook);
	}
	
	public String toString() {
		return new StringBuilder()
				.append("<id>")
				.append(id)
				.append("</id>")
				.append("<termcode>")
				.append(termCode)
				.append("</termcode>")
				.append("<name>")
				.append(name)
				.append("</name>")
				.append("<startdate>")
				.append(format(startDate))
				.append("</startdate>")
				.append("<enddate>")
				.append(format(endDate))
				.append("</enddate>")
				.append("<meetingdays>")
				.append(meetingDays)
				.append("</meetingdays>")
				.append("<meetingtimes>")
				.append(meetingTimes)
				.append("</meetingtimes>")
				.append("\n<students>\n")
				.append(formatStudentRecords())
				.append("\n</students>\n")
				.append("<gradebook>\n")
				.append(formatGradebookRecords())
				.append("\n</gradebook>\n")
				.toString();
	}
	
	/**
	 * Formats the student records.
	 * Call toString on each student record, concatenate it with a new line, collect the result
	 * into a list and return it.
	 * 
	 * @return The formated student records
	 */
	private String formatStudentRecords() {
		return students.stream()
				.map(Student::toString)
				.collect(Collectors.joining("\n"));
	}
	
	/**
	 * Formats the gradebook records
	 * 
	 * @return The formatted gradebook records
	 */
	private String formatGradebookRecords() {
		return gradebook.entrySet().stream()
				.map(e -> String.format("<grade><studentid>%s</studentid><gpa>%s</gpa></grade>", e.getKey(), e.getValue()))
				.collect(Collectors.joining("\n"));
	}
	
}