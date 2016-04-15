package pt.upa.broker.exception;

import pt.upa.broker.exception.BrokerClientException;

public class ArgumentsMissingException extends BrokerClientException {
    private static final long serialVersionUID = 1L;

    public ArgumentsMissingException(){
    }

    @Override
    public String getMessage(){
        return "Argument(s) are missing, expected 2";
    }
}