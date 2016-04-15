package pt.upa.broker.exception;

public class BrokerClientException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public BrokerClientException(){
	}
	
	public BrokerClientException(String msg){
		super(msg);
	}
}