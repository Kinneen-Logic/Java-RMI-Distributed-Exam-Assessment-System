package exceptions;

public class InvalidOptionNumber extends Exception {
		
	
	private static final long serialVersionUID = 1L;

	public InvalidOptionNumber(String reason) {
		super("Invalid Number Chosen");
	}
}

