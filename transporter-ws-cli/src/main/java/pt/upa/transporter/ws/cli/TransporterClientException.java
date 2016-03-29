package pt.upa.transporter.ws.cli;

public class TransporterClientException extends Exception {

    public TransporterClientException() {
    }

    public TransporterClientException(String message) {
        super(message);
    }

    public TransporterClientException(Throwable cause) {
        super(cause);
    }

    public TransporterClientException(String message, Throwable cause) {
        super(message, cause);
    }

}