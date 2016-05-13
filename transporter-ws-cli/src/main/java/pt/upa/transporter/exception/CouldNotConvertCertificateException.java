package pt.upa.transporter.exception;

public class CouldNotConvertCertificateException extends TransporterClientException {

    private static final long serialVersionUID = 1L;


    public CouldNotConvertCertificateException(){ }


    @Override
    public String getMessage(){
        return "This certificate could not be read or parsed: ";
    }
}
