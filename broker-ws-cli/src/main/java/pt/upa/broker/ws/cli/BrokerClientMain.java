package pt.upa.broker.ws.cli;

public class BrokerClientMain {
	
	public static void main(String[] args) {

        try {
			BrokerClient client = new BrokerClient();
		} catch (BrokerClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
