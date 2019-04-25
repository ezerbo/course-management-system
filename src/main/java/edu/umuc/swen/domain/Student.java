package edu.umuc.swen.domain;

import static edu.umuc.swen.domain.util.ParsingUtil.getPropertyValue;
import static java.lang.Integer.parseInt;
import static java.lang.Double.parseDouble;
/**
 * @author ezerbo
 *
 */
public class Student {

	private int id;
	
	private String firstName;
	
	private String lastName;
	
	private double overallGpa;
	
	private String emailAddress;
	
	private Address mailingAddress;
	
	public Student(String line) {
		this.id = parseInt(getPropertyValue(line, "id"));
		this.firstName = getPropertyValue(line, "firstname");
		this.lastName = getPropertyValue(line, "lastname");
		this.overallGpa = parseDouble(getPropertyValue(line, "overallgpa"));
		this.emailAddress = getPropertyValue(line, "emailaddress");
		this.mailingAddress = new Address(getPropertyValue(line, "mailingaddress"));
	}
	
	public Student(int id, String firstName, String lastName,
			double overallGpa, String emailAddress, Address mailingAddress) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.overallGpa = overallGpa;
		this.emailAddress = emailAddress;
		this.mailingAddress = mailingAddress;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}


	/**
	 * @return the overallGpa
	 */
	public double getOverallGpa() {
		return overallGpa;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @return the mailingAddress
	 */
	public Address getMailingAddress() {
		return mailingAddress.clone();
	}

	public String toString() {
		return new StringBuilder()
				.append("<student>")
				.append("<id>")
				.append(id)
				.append("</id>")
				.append("<firstname>")
				.append(firstName)
				.append("</firstname>")
				.append("<lastname>")
				.append(lastName)
				.append("</lastname>")
				.append("<overallgpa>")
				.append(overallGpa)
				.append("</overallgpa>")
				.append("<emailaddress>")
				.append(emailAddress)
				.append("</emailaddress>")
				.append("<mailingaddress>")
				.append(mailingAddress)
				.append("</mailingaddress>")
				.append("</student>")
				.toString();
	}
}