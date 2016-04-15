package pt.upa.transporter.exception;

public class NoEndpointFoundException extends TransporterClientException {
    private static final long serialVersionUID = 1L;

    public NoEndpointFoundException(){
    }

    @Override
    public String getMessage(){
        return "Argument(s) are missing";
    }
}