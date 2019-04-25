package edu.umuc.swen.domain;

import static edu.umuc.swen.domain.util.ParsingUtil.getPropertyValue;

/**
 * @author ezerbo
 *
 */
public class Address {
	
	private String buildingNumber;
	
	private String street;
	
	private String city;
	
	private String state;
	
	private String zipCode;
	
	public Address(String line) {
		this.buildingNumber = getPropertyValue(line, "buildingnumber");
		this.street = getPropertyValue(line, "street");
		this.city = getPropertyValue(line, "city");
		this.state = getPropertyValue(line, "state");
		this.zipCode = getPropertyValue(line, "zipcode");
	}
	
	public Address(String buildingNumber, String street,
			String city, String state, String zipCode) {
		this.buildingNumber = buildingNumber;
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}

	public String getBuildingNumber() {
		return buildingNumber;
	}

	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public Address clone() {
		return new Address(buildingNumber, street, city, state, zipCode);
	}
	
	public String format() {
		return new StringBuilder()
				.append(buildingNumber)
				.append(" " + street)
				.append(" " + city)
				.append(" " + state)
				.append(" " + zipCode)
				.toString();
	}
	
	public String toString() {
		return new StringBuilder()
				.append("<address>")
				.append("<buildingnumber>")
				.append(buildingNumber)
				.append("</buildingnumber>")
				.append("<street>")
				.append(street)
				.append("</street>")
				.append("<city>")
				.append(city)
				.append("</city>")
				.append("<state>")
				.append(state)
				.append("</state>")
				.append("<zipcode>")
				.append(zipCode)
				.append("</zipcode>")
				.append("</address>")
				.toString();
	}

}