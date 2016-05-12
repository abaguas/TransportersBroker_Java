package pt.upa.broker.ws.cli;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.xml.ws.WebServiceException;

import pt.upa.broker.exception.ArgumentsMissingException;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;

public class BrokerClientMain {
	
	public static void main(String[] args) throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, IOException {
		
		if (args.length < 2) {
            throw new ArgumentsMissingException();
        }
		
		System.out.println("Will connect to UpaBroker");
        System.out.println("Enter");
        System.in.read();
		
		BrokerClient client = null;
		System.out.println(args[0]);
		System.out.println(args[1]);
        client = new BrokerClient(args[0], args[1]);
        
        System.out.println("Will request a transport from Faro to Lisboa at price 39");
        System.out.println("Enter");
        System.in.read();
        
        client.clearTransports();
        
        String id = client.requestTransport("Faro", "Lisboa", 39);
        System.out.println("Id: "+ id);
       
        System.out.println("Will list the requested transport");
        System.out.println("Enter");
        System.in.read();
        ArrayList<TransportView> list = (ArrayList<TransportView>) client.listTransports();
        for (TransportView tv: list) {
        	System.out.println(tv.getId());
        	System.out.println(tv.getOrigin());
        	System.out.println(tv.getDestination());
        	System.out.println(tv.getTransporterCompany());
        	System.out.println(tv.getPrice());
        	System.out.println(tv.getState().value());
        	System.out.println(tv.toString());
        }
        
        System.out.println("Will request a transport from Porto to Guarda at price 40");
        System.out.println("Enter");
        System.in.read();
        
        id = client.requestTransport("Porto", "Guarda", 40);
        System.out.println("ID: "+ id);
        
        System.out.println("Will list the requested transports");
        System.out.println("Enter");
        System.in.read();
        list = (ArrayList<TransportView>) client.listTransports();
        for (TransportView tv: list) {
        	System.out.println(tv.getId());
        	System.out.println(tv.getOrigin());
        	System.out.println(tv.getDestination());
        	System.out.println(tv.getTransporterCompany());
        	System.out.println(tv.getPrice());
        	System.out.println(tv.getState().value());
        	System.out.println(tv.toString());
        }
        
        System.out.println("Enter to finish");
        System.in.read();
	}
}
