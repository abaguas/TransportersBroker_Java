package pt.upa.transporter.ws.cli;

import javax.xml.registry.JAXRException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.exception.ArgumentsMissingException;
import pt.upa.transporter.exception.NoEndpointFoundException;

public class TransporterClientMain {
	public static TransporterClient main(String[] args) throws JAXRException, ArgumentsMissingException, NoEndpointFoundException{
		
		if (args.length < 2) {
            throw new ArgumentsMissingException();
        }

        String uddiURL = args[0];
		String name = args[1];
		
	    System.out.printf("Contacting UDDI at %s%n", uddiURL);
		UDDINaming uddiNaming = new UDDINaming(uddiURL);
		System.out.printf("Looking for '%s'%n", name);
		String endpointAddress = uddiNaming.lookup(name);  
	    if (endpointAddress == null) {
	    	throw new NoEndpointFoundException();
	    }
	    else {
	    	return new TransporterClient(endpointAddress);
	    }
	}
}
