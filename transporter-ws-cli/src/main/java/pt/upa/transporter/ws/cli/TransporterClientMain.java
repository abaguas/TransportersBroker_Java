package pt.upa.transporter.ws.cli;

import javax.xml.registry.JAXRException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

public class TransporterClientMain {
	public static void main(String[] args) {
		
		if (args.length < 2) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s url%n", TransporterClientMain.class.getName());
            return;
        }

        String uddiURL = args[0];
		String name = args[1];
		
		try {
			System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = new UDDINaming(uddiURL);
			System.out.printf("Looking for '%s'%n", name);
			String endpointAddress = uddiNaming.lookup(name);  
		    if (endpointAddress == null) {
		    	System.out.println("Not found!");
		    }
		    else {
		    	TransporterClient client = new TransporterClient(endpointAddress);
		    	String a = client.ping("cenas");
		    	System.out.println(a);
		    }
		} catch (JAXRException e1) {
			System.out.println("Connection failed due to JAXException");
		}
	  
	}
}
