package edu.umuc.swen.domain.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author ezerbo
 *
 */
public class ParsingUtil {
	
	private final static SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	
	private ParsingUtil() {} //Utility Class, no instance can be created
	
	/**
	 * @param line String to parse the value of 'propertyName' from
	 * @param property Property for which the value is retrieved
	 * @return the value of 'propertyName'
	 */
	public static String getPropertyValue(String line, String property) {
		try {
			String startingNode = String.format("<%s>", property);
			String endingNode = String.format("</%s>", property);
			return line.substring(line.indexOf(startingNode) + startingNode.length(), line.indexOf(endingNode));
		} catch (StringIndexOutOfBoundsException e) {
			return null; //Return null when property not found
		}
	}
	
	public static Date parseDate(String date) {
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(String.format("Unable to parse date : %s", date));
		}
	}
	
	public static String format(Date date) {
		return formatter.format(date);
	}
	
}
