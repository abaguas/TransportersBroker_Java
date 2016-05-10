package pt.upa.broker.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import java.util.TimerTask;
import java.util.Timer;

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
import java.util.*;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class FrontEnd implements BrokerPortType{
	
	private BrokerPortType port = null;
	private boolean verbose = true;
	private String endpointURL= null;
	private BindingProvider bindingProvider = null;
    private Map<String, Object> requestContext = null;
    private List<String> CONN_TIME_PROPS = null;
    private Timer timer = null;

    public FrontEnd(String uddiURL, String name) {
        lookUp(uddiURL, name);
        createStub();
        setTimeouts();
    }
    
    /** constructor with provided web service URL */
    public FrontEnd(String endpointURL) throws BrokerClientException {
		this.endpointURL = endpointURL;
		createStub();
		setTimeouts();
	}

    private void setTimeouts () {
    	bindingProvider = (BindingProvider) port;
        requestContext = bindingProvider.getRequestContext();
        int connectionTimeout = 2000;
        // The connection timeout property has different names in different versions of JAX-WS
        // Set them all to avoid compatibility issues
        CONN_TIME_PROPS = new ArrayList<String>();
        CONN_TIME_PROPS.add("com.sun.xml.ws.connect.timeout");
        CONN_TIME_PROPS.add("com.sun.xml.internal.ws.connect.timeout");
        CONN_TIME_PROPS.add("javax.xml.ws.client.connectionTimeout");
        // Set timeout until a connection is established (unit is milliseconds; 0 means infinite)
        for (String propName : CONN_TIME_PROPS)
            requestContext.put(propName, connectionTimeout);
        System.out.printf("Set connection timeout to %d milliseconds%n", connectionTimeout);

        int receiveTimeout = 5000;
        // The receive timeout property has alternative names
        // Again, set them all to avoid compability issues
        final List<String> RECV_TIME_PROPS = new ArrayList<String>();
        RECV_TIME_PROPS.add("com.sun.xml.ws.request.timeout");
        RECV_TIME_PROPS.add("com.sun.xml.internal.ws.request.timeout");
        RECV_TIME_PROPS.add("javax.xml.ws.client.receiveTimeout");
        // Set timeout until the response is received (unit is milliseconds; 0 means infinite)
        for (String propName : RECV_TIME_PROPS)
            requestContext.put(propName, 1000);
        System.out.printf("Set receive timeout to %d milliseconds%n", receiveTimeout);
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
		System.out.println("Mandei ganda ping");
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
	
	@Override
	public void updateTransport(TransportView transport) {
		port.updateTransport(transport);
		
	}

	@Override
	public void iAmAlive(String iAmAlive) {
		System.out.println("Settei o timer");

		timer = new Timer();
		TimerTask timerTask = new TimerTask(){

			@Override
			public void run() {
				port.iAmAlive(iAmAlive);
				iAmAlive(iAmAlive);
			}
		};
		timer.schedule(timerTask, 8000);
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

}
