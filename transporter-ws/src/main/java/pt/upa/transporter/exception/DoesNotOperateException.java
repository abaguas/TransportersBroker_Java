package pt.upa.transporter.exception;

import pt.upa.transporter.exception.TransporterServerException;

public class DoesNotOperateException extends TransporterServerException {
    private static final long serialVersionUID = 1L;

    private String name;
    private String origin;
    private String destination;

    public DoesNotOperateException(String name, String origin, String destination){
        this.origin=origin;
        this.destination=destination;
    }

    public String getName(){
    	return name;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public String getDestination() {
        return destination;
    }

    @Override
    public String getMessage(){
        return "The transporter "+getName() + "does not operate from " + getOrigin() + "to " + getDestination();
    }
}