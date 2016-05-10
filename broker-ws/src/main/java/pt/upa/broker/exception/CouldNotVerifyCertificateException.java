package pt.upa.broker.exception;

public class CouldNotVerifyCertificateException extends BrokerServerException {

    private static final long serialVersionUID = 1L;


    public CouldNotVerifyCertificateException(){ }


    @Override
    public String getMessage(){
        return "The verification of this certificate failed";
    }
}