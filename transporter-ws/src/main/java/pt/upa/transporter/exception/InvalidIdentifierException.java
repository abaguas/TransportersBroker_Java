package pt.upa.transporter.exception;

import pt.upa.transporter.exception.TransporterServerException;

public class InvalidIdentifierException extends TransporterServerException {
    private static final long serialVersionUID = 1L;

    private String _id;

    public InvalidIdentifierException(String id){
        _id = id;
    }

    public String getId() {
        return _id;
    }

    @Override
    public String getMessage(){
        return "This identifier is not valid: " + getId();
    }
}