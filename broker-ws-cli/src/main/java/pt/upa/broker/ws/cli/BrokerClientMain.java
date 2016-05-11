package pt.upa.broker.ws.cli;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.xml.ws.WebServiceException;

import pt.upa.broker.exception.ArgumentsMissingException;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;

public class BrokerClientMain {
	
	public static void main(String[] args) throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, IOException {
		
//		if (args.length < 2) {
//            throw new ArgumentsMissingException();
//      }
		
		System.out.println("Will connect to UpaBroker");
        System.out.println("Enter");
        System.in.read();
		
		BrokerClient client = null;
		System.out.println("http://localhost:9090");
		System.out.println("UpaBroker");
        client = new BrokerClient("http://localhost:9090", "UpaBroker");
        
        System.out.println("Will request a transport");
        System.out.println("Enter");
        System.in.read();
        
        String id = client.requestTransport("Faro", "Lisboa", 39);
        System.out.println("Id: "+ id);
        
        System.out.println("Will request another transport");
        System.out.println("Enter");
        System.in.read();
        
        id = client.requestTransport("Faro", "Lisboa", 39);
        
        System.out.println("ID: "+ id);
        
        System.out.println("Enter to finish");
        System.in.read();
	}
}
