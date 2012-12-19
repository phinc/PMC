/*
 * PMCException
 * 
 * Version 1.1
 * 
 * Programmer: Iryna Fedartsova
 * 
 * Date: 07.06.2012
 *
 * PMC application exception.
 */
package by.phinc.pmc.exception;

public class PMCException extends RuntimeException {
	
	private static final long serialVersionUID = 27L;
	
	private Throwable causes[];

	public PMCException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PMCException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	
	public PMCException(String message, Throwable ... causes) {
		super(message, causes[0]);
		this.causes = causes;
	}

	public PMCException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public PMCException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}
	
	
}
