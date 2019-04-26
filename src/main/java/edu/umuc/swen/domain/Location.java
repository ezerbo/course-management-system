package edu.umuc.swen.domain;

import static edu.umuc.swen.domain.util.ParsingUtil.getPropertyValue;

/**
 * Domain class modeling a location
 * 
 * @author ezerbo
 *
 */
public class Location {

	private String roomNumber;
	
	private String buildingName;
	
	private Address address;
	
	public Location(String line) {
		this.roomNumber = getPropertyValue(line, "roomnumber");
		this.buildingName = getPropertyValue(line, "buildingname");
		this.address = new Address(getPropertyValue(line, "address"));
	}
	
	public Location(String roomNumber, String buildingName, Address address) {
		this.roomNumber = roomNumber;
		this.buildingName = buildingName;
		this.address = address;
	}
	
	/**
	 * @return the roomNumber
	 */
	public String getRoomNumber() {
		return roomNumber;
	}

	/**
	 * @param roomNumber the roomNumber to set
	 */
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	/**
	 * @return the buildingName
	 */
	public String getBuildingName() {
		return buildingName;
	}

	/**
	 * @param buildingName the buildingName to set
	 */
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * Formats a location (example: 1019 5E 221-B Baker Street London UK 188000)
	 * 
	 * @return the formatted location  
	 */
	public String format() {
		return new StringBuilder()
				.append(buildingName)
				.append(" " + roomNumber)
				.append(" " + address.format())
				.toString();
	}
	
	public String toString() {
		return new StringBuilder()
				.append("<location>")
				.append("<roomnumber>")
				.append(roomNumber)
				.append("</roomnumber>")
				.append("<buildingname>")
				.append(buildingName)
				.append("</buildingname>")
				.append(address)
				.append("</location>")
				.toString();
	}
}
