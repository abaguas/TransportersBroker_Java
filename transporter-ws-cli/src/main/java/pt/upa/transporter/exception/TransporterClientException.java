package pt.upa.transporter.exception;

public class TransporterClientException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public TransporterClientException(){
	}
	
	public TransporterClientException(String msg){
		super(msg);
	}
}