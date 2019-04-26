package edu.umuc.swen.domain;

import java.util.Date;
import static edu.umuc.swen.domain.util.ParsingUtil.getPropertyValue;

/**
 * Domain class modeling a hybrid course
 * 
 * @author ezerbo
 *
 */
public class HybridCourse extends Course {
	
	private String url;
	
	private Location classroomLocation;

	public HybridCourse(String line) {
		super(line);
		this.url = getPropertyValue(line, "url");
		this.classroomLocation = new Location(getPropertyValue(line, "classroomlocation"));
	}
	
	public HybridCourse(int id, String name, Date startDate, Date endDate, String meetingDays,
			String meetingTimes, String termCode, String url, Location classroomLocation) {
		super(id,name,startDate,endDate,meetingDays,meetingTimes,termCode);
		this.url = url;
		this.classroomLocation = classroomLocation;
	}
	
	/* (non-Javadoc)
	 * @see edu.umuc.swen.domain.Course#generateSchedule()
	 */
	public String generateSchedule() {
		return new StringBuilder()
				.append(super.generateSchedule())
				.append(", ")
				.append("Location : (url: " + url)
				.append(", ")
				.append(classroomLocation.format())
				.append(")")
				.toString();
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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
	
	public String toString() {
		return new StringBuilder()
				.append("<hybridcourse>\n")
				.append(super.toString())
				.append("<url>")
				.append(url)
				.append("</url>\n")
				.append("<classroomlocation>\n")
				.append(classroomLocation)
				.append("\n</classroomlocation>\n")
				.append("</hybridcourse>")
				.toString();
	}

}
