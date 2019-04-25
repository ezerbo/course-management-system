package edu.umuc.swen.error;

/**
 * @author ezerbo
 *
 */
public class CourseOutOfRangeException extends RuntimeException {

	private static final long serialVersionUID = -6153466570470399292L;
	
	public CourseOutOfRangeException(String errorMessage) {
		super(errorMessage);
	}
	
	public String toString() {
		return this.getMessage();
	}

}
