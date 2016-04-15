package pt.upa.broker.ws.cli;

import pt.upa.broker.exception.ArgumentsMissingException;

public class BrokerClientMain {
	
	public static BrokerClient main(String[] args) {
		
		if (args.length < 2) {
            throw new ArgumentsMissingException();
        }
		
		BrokerClient client = null;
		System.out.println(args[0]);
		System.out.println(args[1]);
        client = new BrokerClient(args[0], args[1]);
        
        return client;
	}
}
