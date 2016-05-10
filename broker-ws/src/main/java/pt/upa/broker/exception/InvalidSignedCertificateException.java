package pt.upa.broker.exception;

public class InvalidSignedCertificateException extends BrokerServerException{

    private static final long serialVersionUID = 1L;


    public InvalidSignedCertificateException(){ }


    @Override
    public String getMessage(){
        return "This certificate is not from CA: ";
    }
}