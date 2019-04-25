/**
 * 
 */
package edu.umuc.swen.domain;

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
import java.util.stream.Collectors;

import edu.umuc.swen.error.InvalidStudentOperationException;

/**
 * @author ezerbo
 *
 */
public abstract class Course {
	
	private final static int CLASS_SIZE = 20;
	
	protected int id;
	
	protected String name;
	
	protected Date startDate;
	
	protected Date endDate;
	
	protected String meetingDays;
	
	protected String meetingTimes;
	
	protected List<Student> students = new LinkedList<>();
	
	protected Map<Integer, Double> gradebook = new HashMap<>();
	
	protected String termCode;
	
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
		
		if(students.size() == CLASS_SIZE)
			throw new InvalidStudentOperationException(
					String.format("Unable to add students to this course, the maximum number of students (20) has been reached", format(endDate)));
		
		students.add(student);
	}
	
	/**
	 * @param studentId Identifier of the student (example: 1)
	 * @param gpa New GPA (example: 4.0)
	 */
	public void changeStudentGpa(int studentId, double gpa) {
		if(students.stream().anyMatch(s -> (s.getId() == studentId))) { //If student exists
			gradebook.put(studentId, gpa);
		} else {
			//TODO replace with StudentNotFoundException
			throw new RuntimeException(String.format("No student found with id: %s", studentId));
		}
	}
	
	/**
	 * Removes a student from this course
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
	
	public void loadStudents(String fileName) {
		
	}
	
	private List<Student> parseStudents(String students) {
		return Arrays.stream(students.split("\n"))
				.filter(e -> !e.isEmpty())
				.map(studentLine -> new Student(studentLine))
				.collect(Collectors.toList());
	}
	
	private Map<Integer, Double> parseGradebook(String gradebook) {
		Map<Integer, Double> grades = new HashMap<>();
		Arrays.stream(gradebook.split("\n"))
		.filter(e -> !e.isEmpty())
		.forEach(gradebookEntry -> {
			Integer studentId = parseInt(getPropertyValue(gradebookEntry, "studentid"));
			Double gpa = parseDouble(getPropertyValue(gradebookEntry, "gpa"));
			grades.put(studentId, gpa);
		});
		return grades;
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
	 * @return
	 */
	private String formatStudentRecords() {
		return students.stream()
				.map(Student::toString)
				.collect(Collectors.joining("\n"));
	}
	
	/**
	 * @return
	 */
	private String formatGradebookRecords() {
		return gradebook.entrySet().stream()
				.map(e -> String.format("<grade><studentid>%s</studentid><gpa>%s</gpa></grade>", e.getKey(), e.getValue()))
				.collect(Collectors.joining("\n"));
	}
	
	protected boolean hasStarted() {
		return new Date().after(startDate);
	}
	
	protected boolean hasEnded() {
		return new Date().after(endDate);
	}
	
	/**
	 * @return the termCode
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
	
}