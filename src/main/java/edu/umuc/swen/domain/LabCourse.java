package edu.umuc.swen.domain;

import java.util.Date;

import edu.umuc.swen.error.InvalidStudentOperationException;

import static edu.umuc.swen.domain.util.ParsingUtil.format;
import static edu.umuc.swen.domain.util.ParsingUtil.getPropertyValue;

/**
 * @author ezerbo
 *
 */
public class LabCourse extends Course {
	
	private final static int CLASS_SIZE = 10; //Class size is 10 for LabCourses
	
	private Location classroomLocation;
	
	private Location labRoomLocation;

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
	
	public void addStudent(Student student) {
		if(hasEnded()) 
			throw new InvalidStudentOperationException(
					String.format("Unable to add students to this course, it has already ended on %s", format(endDate)));
		
		if(students.size() == CLASS_SIZE)
			throw new InvalidStudentOperationException(
					String.format("Unable to add students to this course, the maximum number of students (20) has been reached", format(endDate)));
		
		students.add(student);
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

}
