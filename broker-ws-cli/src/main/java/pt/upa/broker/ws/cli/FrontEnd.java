package pt.upa.broker.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;
import java.util.TimerTask;
import java.util.Timer;

import java.util.List;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.broker.exception.BrokerClientException;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;

import java.net.SocketTimeoutException;
import java.util.*;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class FrontEnd implements BrokerPortType{
	
	private String uddiURL = null;
	private String searchName = null;
	private BrokerPortType port = null;
	private boolean verbose = true;
	private String endpointURL= null;
	private BindingProvider bindingProvider = null;
    private Map<String, Object> requestContext = null;
    private List<String> CONN_TIME_PROPS = null;
    private Timer timer = null;

    public FrontEnd(String uddiURL, String searchName) {
    	this.uddiURL = uddiURL;
    	this.searchName = searchName;
        lookUp(uddiURL, searchName, false);
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
    	if (getSearchName().equals("UpaBroker")) {
	    	bindingProvider = (BindingProvider) port;
	        requestContext = bindingProvider.getRequestContext();
	        int connectionTimeout = 10000;
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
	
	        int receiveTimeout = 10000;
	        // The receive timeout property has alternative names
	        // Again, set them all to avoid compability issues
	        final List<String> RECV_TIME_PROPS = new ArrayList<String>();
	        RECV_TIME_PROPS.add("com.sun.xml.ws.request.timeout");
	        RECV_TIME_PROPS.add("com.sun.xml.internal.ws.request.timeout");
	        RECV_TIME_PROPS.add("javax.xml.ws.client.receiveTimeout");
	        // Set timeout until the response is received (unit is milliseconds; 0 means infinite)
	        for (String propName : RECV_TIME_PROPS)
	            requestContext.put(propName, receiveTimeout);
	        System.out.printf("Set receive timeout to %d milliseconds%n", receiveTimeout);
    	}
    }

    public void lookUp (String uddiURL, String searchName, boolean sleep) {
		if (sleep) {
			System.out.println("Sleeping in lookup");
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				String msg = String.format("Thread exception!");
				throw new BrokerClientException(msg, e);
			}
			System.out.println("Woke up");
		}
		if (verbose){
	    	System.out.printf("Contacting UDDI at %s%n", uddiURL);
		}
		try {
    	UDDINaming uddiNaming = new UDDINaming(uddiURL);
    	if (uddiNaming == null) {System.out.println("cenas más");}
		if (verbose){
			System.out.printf("Looking for '%s'%n", searchName);
		}
			endpointURL = uddiNaming.lookup(searchName);
		} catch (JAXRException e) {
			String msg = String.format("Client failed lookup on UDDI at %s!", uddiURL);
			throw new BrokerClientException(msg, e);
		}
			
    	
        if (endpointURL == null) {
        	String msg = String.format("Service with name %s not found on UDDI at %s", searchName, uddiURL);
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
		try {
			return port.ping(name);
			
        } catch(WebServiceException wse) {
            System.out.println("Caught: " + wse);
            Throwable cause = wse.getCause();
            if (cause != null && cause instanceof SocketTimeoutException) {
            	System.out.println("Timeout, ardeu! Vou procurar no UDDI o Broker");
                lookUp(getUddiURL(), getSearchName(), true);
                createStub();
                return port.ping(name);
            }
            else {
            	System.out.println("Connexão, ardeu! Vou procurar no UDDI o Broker");
                lookUp(getUddiURL(), getSearchName(), true);
                createStub();
                System.out.println("A fazer novo ping ------------------------------");
                return port.ping(name);
            }
        }	
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		try {
			return port.requestTransport(origin, destination, price);
			
        } catch(WebServiceException wse) {
            System.out.println("Caught: " + wse);
            Throwable cause = wse.getCause();
            if (cause != null && cause instanceof SocketTimeoutException) {
            	System.out.println("Timeout, ardeu! Vou procurar no UDDI o Broker");
                lookUp(getUddiURL(), getSearchName(), true);
                createStub();
                return port.requestTransport(origin, destination, price);
            }
            else {
            	System.out.println("Connecção, ardeu! Vou procurar no UDDI o Broker");
                lookUp(getUddiURL(), getSearchName(), true);
                createStub();
                System.out.println("A fazer novo request ------------------------------");
                return requestTransport(origin, destination, price);
            }
        }	
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		try {
			return port.viewTransport(id);
			
        } catch(WebServiceException wse) {
            System.out.println("Caught: " + wse);
            Throwable cause = wse.getCause();
            if (cause != null && cause instanceof SocketTimeoutException) {
            	System.out.println("Timeout, ardeu! Vou procurar no UDDI o Broker");
                lookUp(getUddiURL(), getSearchName(), true);
                createStub();
                return port.viewTransport(id);
            }
            else {
            	System.out.println("Connecção, ardeu! Vou procurar no UDDI o Broker");
                lookUp(getUddiURL(), getSearchName(), true);
                createStub();
                System.out.println("A fazer novo view ------------------------------");
                return port.viewTransport(id);
            }
        }
	}

	@Override
	public List<TransportView> listTransports() {
		try {
			return port.listTransports();
			
        } catch(WebServiceException wse) {
            System.out.println("Caught: " + wse);
            Throwable cause = wse.getCause();
            if (cause != null && cause instanceof SocketTimeoutException) {
            	System.out.println("Timeout, ardeu! Vou procurar no UDDI o Broker");
                lookUp(getUddiURL(), getSearchName(), true);
                createStub();
                return port.listTransports();
            }
            else {
            	System.out.println("Connecção, ardeu! Vou procurar no UDDI o Broker");
                lookUp(getUddiURL(), getSearchName(), true);
                createStub();
                System.out.println("A fazer novo list ------------------------------");
                return port.listTransports();
            }
        }
	}

	@Override
	public void clearTransports() {
		try {
			port.clearTransports();
			
        } catch(WebServiceException wse) {
            System.out.println("Caught: " + wse);
            Throwable cause = wse.getCause();
            if (cause != null && cause instanceof SocketTimeoutException) {
            	System.out.println("Timeout, ardeu! Vou procurar no UDDI o Broker");
                lookUp(getUddiURL(), getSearchName(), true);
                createStub();
                port.clearTransports();
            }
            else {
            	System.out.println("Connecção, ardeu! Vou procurar no UDDI o Broker");
                lookUp(getUddiURL(), getSearchName(), true);
                createStub();
                System.out.println("A fazer novo clear ------------------------------");
                port.clearTransports();
            }
        }
	}
	
	
	@Override
	public void updateTransport(TransportView transportView, String endpoint) {
		port.updateTransport(transportView, endpoint);
	}

	@Override
	public void iAmAlive(String iAmAlive) {
		System.out.println("I am alive will be sent in 3 seconds");

		timer = new Timer();
		TimerTask timerTask = new TimerTask(){

			@Override
			public void run() {
				port.iAmAlive(iAmAlive);
				System.out.println("I am alive sent");
				iAmAlive(iAmAlive);
			}
		};
		timer.schedule(timerTask, 3000);
	}

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public String getUddiURL() {
		return uddiURL;
	}

	public void setUddiURL(String uddiURL) {
		this.uddiURL = uddiURL;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

}
