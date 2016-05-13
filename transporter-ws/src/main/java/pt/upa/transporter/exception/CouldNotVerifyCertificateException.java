package pt.upa.transporter.exception;

public class CouldNotVerifyCertificateException extends TransporterServerException {

    private static final long serialVersionUID = 1L;


    public CouldNotVerifyCertificateException(){ }
    

    @Override
    public String getMessage(){
        return "The verification of this certificate failed";
    }
}
