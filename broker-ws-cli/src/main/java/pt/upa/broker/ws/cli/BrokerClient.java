package pt.upa.broker.ws.cli;

import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.BrokerService;

public class BrokerClient implements BrokerPortType{

	
	/** WS service */
    private BrokerService service = null;

    /** WS port (interface) */
    private BrokerPortType port = null;
    private BindingProvider bindingProvider = null;


    /** WS endpoint address */
    // default value is defined by WSDL
    private String wsURL = null;

    /** retrieve WS endpoint address */
    public String getEndpointAddress() {
        if (wsURL != null) {
            return wsURL;
        } else {
            Map<String, Object> requestContext = bindingProvider.getRequestContext();
            return (String) requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        }
    }


    /** output option **/
    private boolean verbose = false;

    public boolean isVerbose() {
        return verbose;
    }
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }


    /** constructor with provided web service URL */
    public BrokerClient(String wsURL) throws BrokerClientException {
        this.wsURL = wsURL;
        createStub();
    }

    /** default constructor uses default endpoint address */
    public BrokerClient() throws BrokerClientException {
        createStub();
    }


    /** Stub creation and configuration */
    protected void createStub() {
        if (verbose)
            System.out.println("Creating stub ...");
        service = new BrokerService();
        port = service.getBrokerPort();
        bindingProvider = (BindingProvider) port;

        if (wsURL != null) {
            if (verbose)
                System.out.println("Setting endpoint address ...");
            Map<String, Object> requestContext = bindingProvider.getRequestContext();
            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsURL);
        }
    }
	
	
	
	
	
	
	@Override
	public String ping(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TransportView> listTransports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearTransports() {
		// TODO Auto-generated method stub
		
	}

}
