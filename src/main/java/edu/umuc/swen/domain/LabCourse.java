package edu.umuc.swen.domain;

import java.util.Date;
import java.util.List;

import edu.umuc.swen.error.InvalidStudentOperationException;

import static edu.umuc.swen.domain.util.FileUtil.loadFromFile;
import static edu.umuc.swen.domain.util.ParsingUtil.format;
import static edu.umuc.swen.domain.util.ParsingUtil.getPropertyValue;

/**
 * Domain class modeling a lab course
 * 
 * @author ezerbo
 *
 */
public class LabCourse extends Course {
	
	/**
	 * Maximum number of students allowed into a lab course, set to 10
	 */
	private final static int MAX_NUMBER_OF_STUDENTS = 10; //Class size is 10 for LabCourses
	
	private Location classroomLocation;
	
	private Location labRoomLocation;

	/**
	 * Creates an instance of LabCourse using its string representation
	 * 
	 * @param line
	 */
	public LabCourse(String line) {
		super(line);
		this.classroomLocation = new Location(getPropertyValue(line, "classroomlocation"));
		this.labRoomLocation = new Location(getPropertyValue(line, "labroomlocation"));
	}
	
	public LabCourse(int id, String name, Date startDate, Date endDate, String meetingDays,
			String meetingTimes, String termCode, Location classroomLocation, Location labRoomLocation) {
		super(id,name,startDate,endDate,meetingDays,meetingTimes,termCode);
		this.classroomLocation = classroomLocation;
		this.labRoomLocation = labRoomLocation;
	}
	
	/* (non-Javadoc)
	 * @see edu.umuc.swen.domain.Course#addStudent(edu.umuc.swen.domain.Student)
	 */
	public void addStudent(Student student) {
		if(hasEnded()) 
			throw new InvalidStudentOperationException(
					String.format("Unable to add students to this course, it has already ended on %s", format(endDate)));
		
		if(isCourseFull())
			throw new InvalidStudentOperationException(
					String.format("Unable to add students to this course, the maximum number of students (10) has been reached", format(endDate)));
		
		students.add(student);
	}
	
	/* (non-Javadoc)
	 * @see edu.umuc.swen.domain.Course#loadStudents(java.lang.String)
	 */
	public void loadStudents(String fileName) {
		if(hasEnded()) 
			throw new InvalidStudentOperationException(
					String.format("Unable to add students to this course, it has already ended on %s", format(endDate)));
		
		if(isCourseFull())
			throw new InvalidStudentOperationException(
					String.format("Unable to add students to this course, the maximum number of students (20) has been reached", format(endDate)));
		
		String studentLines = getPropertyValue(loadFromFile(fileName), "students");
		List<Student> laodedStudents = parseStudents(studentLines);
		if(laodedStudents.size() + students.size() > MAX_NUMBER_OF_STUDENTS) {
			throw new InvalidStudentOperationException("Unable to add students to this course, the maximum number of students (20) has been reached");
		}
		students.addAll(laodedStudents);
	}

	/**
	 * @return the classroomLocation
	 */
	public Location getClassroomLocation() {
		return classroomLocation;
	}

	/**
	 * @param classroomLocation the classroomLocation to set
	 */
	public void setClassroomLocation(Location classroomLocation) {
		this.classroomLocation = classroomLocation;
	}

	/**
	 * @return the labRoomLocation
	 */
	public Location getLabRoomLocation() {
		return labRoomLocation;
	}

	/**
	 * @param labRoomLocation the labRoomLocation to set
	 */
	public void setLabRoomLocation(Location labRoomLocation) {
		this.labRoomLocation = labRoomLocation;
	}
	
	/* (non-Javadoc)
	 * @see edu.umuc.swen.domain.Course#generateSchedule()
	 */
	public String generateSchedule() {
		return new StringBuilder()
				.append(super.generateSchedule())
				.append(", ")
				.append("Location : (class: " + classroomLocation.format())
				.append(", lab: ")
				.append(labRoomLocation.format())
				.append(")")
				.toString();
	}
	
	public String toString() {
		return new StringBuilder()
				.append("<labcourse>\n")
				.append(super.toString())
				.append("<classroomlocation>\n")
				.append(classroomLocation)
				.append("\n</classroomlocation>\n")
				.append("<labroomlocation>\n")
				.append(labRoomLocation)
				.append("\n</labroomlocation>\n")
				.append("</labcourse>")
				.toString();
	}
	
	/**
	 * Test whether a course is full (maximum number of students reached)
	 * 
	 * @return indicator
	 */
	public boolean isCourseFull() {
		return students.size() == MAX_NUMBER_OF_STUDENTS;
	}

}
