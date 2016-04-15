package pt.upa.broker.exception;

public class NoEndpointFoundException extends BrokerClientException {
    private static final long serialVersionUID = 1L;

    public NoEndpointFoundException(){
    }

    @Override
    public String getMessage(){
        return "Argument(s) are missing";
    }
}