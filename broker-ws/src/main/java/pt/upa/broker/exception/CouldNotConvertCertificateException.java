package pt.upa.broker.exception;

public class CouldNotConvertCertificateException extends BrokerServerException {

    private static final long serialVersionUID = 1L;


    public CouldNotConvertCertificateException(){ }


    @Override
    public String getMessage(){
        return "This certificate could not be read or parsed: ";
    }
}