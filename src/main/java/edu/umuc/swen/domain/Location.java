package edu.umuc.swen.domain;

import static edu.umuc.swen.domain.util.ParsingUtil.getPropertyValue;

/**
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

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getBuildingName() {
		return buildingName;
	}

	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
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
