package pt.upa.transporter.exception;

import pt.upa.transporter.exception.TransporterServerException;

public class InvalidSignedCertificateException extends TransporterServerException{

    private static final long serialVersionUID = 1L;


    public InvalidSignedCertificateException(){ }


    @Override
    public String getMessage(){
        return "This certificate is not from CA: ";
    }
}