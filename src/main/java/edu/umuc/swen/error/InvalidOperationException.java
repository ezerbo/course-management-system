package edu.umuc.swen.error;

/**
 * @author ezerbo
 *
 */
public class InvalidOperationException extends RuntimeException {

	private static final long serialVersionUID = -2803823085606452694L;
	
	public InvalidOperationException(String errorMessage) {
		super(errorMessage);
	}
	
	public String toString() {
		return this.getMessage();
	}

}
