package edu.umuc.swen.domain;

import java.util.Date;
import static edu.umuc.swen.domain.util.ParsingUtil.getPropertyValue;

/**
 * Domain class modeling an online course
 * 
 * @author ezerbo
 *
 */
public class OnlineCourse extends Course {
	
	/**
	 * URL to the course
	 */
	private String url;

	/**
	 * Creates an instance of OnlineCourse using its string representation
	 * 
	 * @param line string to parse online course data from
	 */
	public OnlineCourse(String line) {
		super(line);
		this.url = getPropertyValue(line, "url");
	}
	
	public OnlineCourse(int id, String url, String name, Date startDate,
			Date endDate, String meetingDays, String termCode, String meetingTimes) {
		super(id,name,startDate,endDate,meetingDays,meetingTimes,termCode);
		this.url = url;
	}
	
	public String generateSchedule() {
		return new StringBuilder()
				.append(super.generateSchedule())
				.append(", ")
				.append("Location : " + url)
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
	
	public String toString() {
		return new StringBuilder()
				.append("<onlinecourse>\n")
				.append(super.toString())
				.append("<url>")
				.append(url)
				.append("</url>\n")
				.append("</onlinecourse>")
				.toString();
	}

}
