package pt.upa.transporter.exception;

public abstract class TransporterServerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TransporterServerException(){
	}
	
	public TransporterServerException(String msg){
		super(msg);
	}
}