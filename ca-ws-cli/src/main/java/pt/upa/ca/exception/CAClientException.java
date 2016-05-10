package pt.upa.ca.exception;

public class CAClientException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CAClientException(){
	}
	
	public CAClientException(String msg){
		super(msg);
	}
	
	public CAClientException(String msg, Throwable cause){
		super(msg, cause);
	}

}