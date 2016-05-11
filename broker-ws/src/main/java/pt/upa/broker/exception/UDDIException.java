package pt.upa.broker.exception;

public class UDDIException extends BrokerServerException {
	private static final long serialVersionUID = 1L;
	
    public UDDIException(){}

    @Override
    public String getMessage(){
        return "UDDI could not be resolved or listed";
    }
}
