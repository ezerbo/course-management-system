package edu.umuc.swen.error;

/**
 * @author ezerbo
 *
 */
public class InvalidStudentOperationException extends RuntimeException {

	private static final long serialVersionUID = 5023306205320765440L;
	
	public InvalidStudentOperationException(String errorMessage) {
		super(errorMessage);
	}
	
	public String toString() {
		return this.getMessage();
	}

}
