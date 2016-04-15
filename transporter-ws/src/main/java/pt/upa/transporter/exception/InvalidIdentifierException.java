package pt.upa.transporter.exception;

import pt.upa.transporter.exception.TransporterServerException;

public class InvalidIdentifierException extends TransporterServerException {
    private static final long serialVersionUID = 1L;

    private String _name;

    public InvalidIdentifierException(String name){
        _name=name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public String getMessage(){
        return "This identifier is not valid: " + getName();
    }
}