package pt.upa.broker.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.exception.BrokerClientException;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class BrokerClient implements BrokerPortType{
	
	private BrokerPortType port;
	private boolean verbose = false;
	private String endpointURL= null;

    public BrokerClient(String uddiURL, String name) {
        lookUp(uddiURL, name);
        createStub();
    }
    
    /** constructor with provided web service URL */
    public BrokerClient(String endpointURL) throws BrokerClientException {
		this.endpointURL = endpointURL;
		createStub();
	}

    
    public void lookUp (String uddiURL, String name) {
    	try {
    		if (verbose){
    	    	System.out.printf("Contacting UDDI at %s%n", uddiURL);
    		}
        	UDDINaming uddiNaming = new UDDINaming(uddiURL);
    		if (verbose){
    			System.out.printf("Looking for '%s'%n", name);
    		}
    		endpointURL = uddiNaming.lookup(name);
    	}catch (Exception e){
    		String msg = String.format("Client failed lookup on UDDI at %s!", uddiURL);
			throw new BrokerClientException(msg, e);
    	}
    	
        if (endpointURL == null) {
        	String msg = String.format(
					"Service with name %s not found on UDDI at %s", name, uddiURL);
			throw new BrokerClientException(msg);
        }
    }
    
    public void createStub() {
    	if (verbose){
    		System.out.println("Creating stub ...");
    	}
    	BrokerService service = new BrokerService();
        port = service.getBrokerPort();
        
        if (port!=null){
	        if (verbose) {
	        	System.out.println("Setting endpoint address ...");
	        }
	        BindingProvider bindingProvider = (BindingProvider) port;
		    Map<String, Object> requestContext = bindingProvider.getRequestContext();
		    requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointURL);
        }
    }

	
	
	@Override
	public String ping(String name) {
		return port.ping(name);
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		return port.requestTransport(origin, destination, price);
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		return port.viewTransport(id);
	}

	@Override
	public List<TransportView> listTransports() {
		return port.listTransports();
	}

	@Override
	public void clearTransports() {
		port.clearTransports();
		
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}


	@Override
	public void updateTransport(TransportView transport) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void iAmAlive(String iAmAlive) {
		// TODO Auto-generated method stub
		
	}

}
