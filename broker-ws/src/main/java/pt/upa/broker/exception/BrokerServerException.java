package pt.upa.broker.exception;

public abstract class BrokerServerException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BrokerServerException(){
	}
	
	public BrokerServerException(String msg){
		super(msg);
	}
}