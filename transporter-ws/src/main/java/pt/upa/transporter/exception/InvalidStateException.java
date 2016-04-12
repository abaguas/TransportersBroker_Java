package pt.upa.transporter.exception;

import pt.upa.transporter.exception.TransporterServerException;

public class InvalidStateException extends TransporterServerException {
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