package pt.upa.transporter.ws.cli;

public class TransporterClientMain {
	public static void main(String[] args) {

        try {
			TransporterClient client = new TransporterClient("");
		} catch (TransporterClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
