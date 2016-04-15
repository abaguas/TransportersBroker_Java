package pt.upa.transporter.exception;

public class ArgumentsMissingException extends TransporterClientException {
    private static final long serialVersionUID = 1L;

    public ArgumentsMissingException(){
    }

    @Override
    public String getMessage(){
        return "Argument(s) are missing";
    }
}