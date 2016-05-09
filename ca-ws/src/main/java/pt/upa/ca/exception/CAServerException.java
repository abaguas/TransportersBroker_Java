package pt.upa.ca.exception;

public class CAServerException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public CAServerException(){}
	
	public CAServerException(String msg){
		super(msg);
	}
	
}
