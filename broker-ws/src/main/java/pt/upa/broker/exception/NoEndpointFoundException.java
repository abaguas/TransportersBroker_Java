package pt.upa.broker.exception;

public class NoEndpointFoundException extends BrokerServerException {
    private static final long serialVersionUID = 1L;

    public NoEndpointFoundException(){
    }

    @Override
    public String getMessage(){
        return "No endpoint was found";
    }
}