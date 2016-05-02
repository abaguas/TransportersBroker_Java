package pt.upa.transporter.exception;

import pt.upa.transporter.exception.TransporterServerException;

public class NoAvailableIdentifierException extends TransporterServerException {
    private static final long serialVersionUID = 1L;

    private String name;
    
    private int id;
    
    public NoAvailableIdentifierException(String name, int id){
        this.name=name;
    	this.id=id-1;
    }

    public String getName(){
    	return name;
    }
    
    public int getId(){
    	return id;
    }

    @Override
    public String getMessage(){
        return "The transporter "+ getName() + "does reached its maximum identifier" + getId();
    }
}
