package pt.upa.broker.exception;

import pt.upa.broker.exception.BrokerServerException;

public class InvalidStateException extends BrokerServerException {
    private static final long serialVersionUID = 1L;

    private String _name;

    public InvalidStateException(String name){
        _name=name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public String getMessage(){
        return "This state isnt valid: " + getName();
    }
}