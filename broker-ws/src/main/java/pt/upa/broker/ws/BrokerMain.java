package pt.upa.broker.ws;

import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

public class BrokerMain {
    public static void main(String[] args) {
    	// Check arguments
        if (args.length < 4) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s url%n", BrokerMain.class.getName());
            return;
        }

        BrokerPort bp = null;
        String uddiURL = args[0];
		String name = args[1];
		if (name.equals("UpaBroker1")) {
			name = "UpaBroker";
		}
		String url = args[2];
		int nap = Integer.parseInt(args[3]);
		
		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
        try {
        	bp = new BrokerPort(name, uddiURL, url, nap);
            endpoint = Endpoint.create(bp);

            // publish endpoint
            System.out.printf("Starting %s%n", url);
            endpoint.publish(url);
            
            // publish to UDDI
         	System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
         	uddiNaming = new UDDINaming(uddiURL);
         	uddiNaming.rebind(name, url);


            // wait
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            bp.init();
            System.in.read();
            

        } catch (Exception e) {
			System.out.printf("Caught exception: %s%n", e);
			e.printStackTrace();

		} finally {
			try {
				if (endpoint != null) {
					// stop endpoint
					endpoint.stop();
					System.out.printf("Stopped %s%n", url);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when stopping: %s%n", e);
			}
			try {
				if (uddiNaming != null) {
					// delete from UDDI
					uddiNaming.unbind("UpaBroker");
					System.out.printf("Deleted '%s' from UDDI%n", name);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
		}

    }
}