package pt.upa.transporter.ws;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.registry.JAXRException;
import javax.xml.ws.Endpoint;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;


public class TransporterMain {

    public static void main(String[] args) {
    	// Check arguments
        if (args.length < 3) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s url%n", TransporterMain.class.getName());
            return;
        }

        String uddiURL = args[0];
		String name = args[1];
		String url = args[2];

		Endpoint endpoint = null;
		UDDINaming uddiNaming = null;
        try {
        	TransporterPort tp = new TransporterPort(name,uddiURL);
            endpoint = Endpoint.create(tp);
            
           
            
            // publish endpoint
            System.out.printf("Starting %s%n", url);
            endpoint.publish(url);
            
            // publish to UDDI
         	System.out.printf("Publishing '%s' to UDDI at %s%n", name, uddiURL);
         	uddiNaming = new UDDINaming(uddiURL);
         	uddiNaming.rebind(name, url);
         	//tp.init(); FIXME


            // wait
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();

        } catch (JAXRException jaxre) {
			System.out.printf("Caught exception in registry: %s%n", jaxre);
			jaxre.printStackTrace();
        } catch (IOException ioe) {
        	System.out.printf("Could not read input: %s%n", ioe);
        	ioe.printStackTrace();
        	
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
					uddiNaming.unbind(name);
					System.out.printf("Deleted '%s' from UDDI%n", name);
				}
			} catch (Exception e) {
				System.out.printf("Caught exception when deleting: %s%n", e);
			}
		}

    }
}