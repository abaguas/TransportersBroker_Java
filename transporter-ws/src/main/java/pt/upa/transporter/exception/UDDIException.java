package pt.upa.transporter.exception;

public class UDDIException extends TransporterServerException {
	private static final long serialVersionUID = 1L;
	
    public UDDIException(){}

    public String getMessage(){
        return "UDDI could not be resolved or listed";
    }

}
