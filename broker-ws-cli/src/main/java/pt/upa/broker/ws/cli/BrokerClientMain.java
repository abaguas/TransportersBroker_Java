package pt.upa.broker.ws.cli;

import java.net.SocketTimeoutException;

import javax.xml.ws.WebServiceException;

import pt.upa.broker.exception.ArgumentsMissingException;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;

public class BrokerClientMain {
	
	public static void main(String[] args) throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		
		if (args.length < 2) {
            throw new ArgumentsMissingException();
        }
		
		BrokerClient client = null;
		System.out.println(args[0]);
		System.out.println(args[1]);
        client = new BrokerClient(args[0], args[1]);
        
     // call using set endpoint address
    	try {
    	    String result = client.requestTransport("", "", 1);
    	    System.out.println(result);
    	
    	} catch(WebServiceException wse) {
    	    System.out.println("Caught: " + wse);
    	    Throwable cause = wse.getCause();
    	    if (cause != null && cause instanceof SocketTimeoutException) {
    	        System.out.println("The cause was a timeout exception: " + cause);
    	    }

    	
    	}
	}
}
