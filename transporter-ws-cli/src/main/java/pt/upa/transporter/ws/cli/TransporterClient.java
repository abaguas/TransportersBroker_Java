package pt.upa.transporter.ws.cli;

import java.util.List;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

public class TransporterClient implements TransporterPortType {
	
/*
    // WS service 
    private TransporterService service = null;

    // WS port (interface) 
    private TransporterPortType port = null;
    private BindingProvider bindingProvider = null;


    ///** WS endpoint address 
    // default value is defined by WSDL
    private String wsURL = null;

    ///** retrieve WS endpoint address 
    public String getEndpointAddress() {
        if (wsURL != null) {
            return wsURL;
        } else {
            Map<String, Object> requestContext = bindingProvider.getRequestContext();
            return (String) requestContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        }
    }


    ///** output option 
    private boolean verbose = false;

    public boolean isVerbose() {
        return verbose;
    }
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }


    ///** constructor with provided web service URL 
    public TransporterClient(String wsURL) throws TransporterClientException {
        this.wsURL = wsURL;
        createStub();
    }

    ///** default constructor uses default endpoint address 
    public TransporterClient() throws TransporterClientException {
        createStub();
    }


    ///** Stub creation and configuration 
    protected void createStub() {
        if (verbose)
            System.out.println("Creating stub ...");
        service = new TransporterService();
        port = service.getTransporterPort();
        bindingProvider = (BindingProvider) port;

        if (wsURL != null) {
            if (verbose)
                System.out.println("Setting endpoint address ...");
            Map<String, Object> requestContext = bindingProvider.getRequestContext();
            requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsURL);
        }
    }
*/	
	
	private String endpointURL;
	private TransporterPortType port;

    public TransporterClient(String uddiURL, String name) throws TransporterClientException {
        try {
			endpointURL = lookUp(uddiURL, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        createStub(endpointURL);
    }
    
    public String lookUp (String uddiURL, String name) throws Exception{ //má prática?
    	System.out.printf("Contacting UDDI at %s%n", uddiURL);
    	UDDINaming uddiNaming = new UDDINaming(uddiURL);
    	System.out.printf("Looking for '%s'%n", name);
        String endpointAddress = uddiNaming.lookup(name);
        
        if (endpointAddress == null) {
            System.out.println("Not found!");
            return null;
        } else {
            return endpointAddress;
        }
    }
    
    public void createStub(String endpointURL) {
    	System.out.println("Creating stub ...");
    	TransporterService service = new TransporterService();
        TransporterPortType port = service.getTransporterPort();
        
	    System.out.println("Setting endpoint address ...");
	    BindingProvider bindingProvider = (BindingProvider) port;
	    Map<String, Object> requestContext = bindingProvider.getRequestContext();
	    requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointURL);
	    
	    this.port = port;
    }
    
	@Override
	public String ping(String name) {
		return port.ping("Ping");
	}//é preciso apanhar aqui uma exceçao?

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobView jobStatus(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<JobView> listJobs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearJobs() {
		// TODO Auto-generated method stub
		
	}

	// TODO

}
