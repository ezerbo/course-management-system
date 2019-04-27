package edu.umuc.swen.domain;

import static edu.umuc.swen.domain.util.ParsingUtil.getPropertyValue;

/**
 * Domain class modeling an address
 * 
 * @author ezerbo
 *
 */
public class Address {
	
	/**
	 * The building number
	 */
	private String buildingNumber;
	
	/**
	 * The street
	 */
	private String street;
	
	/**
	 * The city
	 */
	private String city;
	
	/**
	 * The state
	 */
	private String state;
	
	/**
	 * The postal code
	 */
	private String zipCode;
	
	/**
	 * Create an instance of Address using its string representation
	 * 
	 * @param line String to parse address data from
	 */
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

	/**
	 * @return buildingNumber The building number
	 */
	public String getBuildingNumber() {
		return buildingNumber;
	}

	/**
	 * @param buildingNumber The building number
	 */
	public void setBuildingNumber(String buildingNumber) {
		this.buildingNumber = buildingNumber;
	}

	/**
	 * @return street The street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street The street
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return city The city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city The city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return state The state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state The state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return zipCode The zip code
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode The zip code
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public Address clone() {
		return new Address(buildingNumber, street, city, state, zipCode);
	}
	
	/**
	 * Formats the current address (example: 221-B Baker Street London UK 188000)
	 * 
	 * @return formattedAddress
	 */
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