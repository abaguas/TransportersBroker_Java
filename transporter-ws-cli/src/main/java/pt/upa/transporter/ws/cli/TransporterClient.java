package pt.upa.transporter.ws.cli;

import java.util.List;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import pt.upa.transporter.exception.*;
import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

public class TransporterClient implements TransporterPortType {	
	
	private TransporterPortType port;
	
	/** WS endpoint address */
	private String endpointURL = null;
	
	/** output option **/
	private boolean verbose = false;

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

    public TransporterClient(String endpointURL) {
        createStub(endpointURL);
    }
    
    /** constructor with provided UDDI location and name for tests */
	public TransporterClient(String uddiURL, String wsName){
		uddiLookup(uddiURL, wsName);
		createStub(endpointURL);
	}
	
	/** UDDI lookup for tests */
	/** UDDI lookup */
	private void uddiLookup(String uddiURL, String wsName) throws TransporterClientException {
		try {
			if (verbose){
				System.out.printf("Contacting UDDI at %s%n", uddiURL);
			}
			UDDINaming uddiNaming = new UDDINaming(uddiURL);

			if (verbose) {
				System.out.printf("Looking for '%s'%n", wsName);
			}
			endpointURL = uddiNaming.lookup(wsName);

		} catch (Exception e) {
			String msg = String.format("Client failed lookup on UDDI at %s!", uddiURL);
			throw new TransporterClientException(msg, e);
		}

		if (endpointURL == null) {
			String msg = String.format("Service with name %s not found on UDDI at %s", wsName, uddiURL);
			throw new TransporterClientException(msg);
		}
	}
    
    public void createStub(String endpointURL) {
    	if (verbose) {
    		System.out.println("Creating stub ...");
    	}
    	TransporterService service = new TransporterService();
        port = service.getTransporterPort();
        
        if (endpointURL != null) {
        	if (verbose){
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
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		return port.requestJob(origin, destination, price);
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		return port.decideJob(id, accept);
	}

	@Override
	public JobView jobStatus(String id) {
		return jobStatus(id);
	}

	@Override
	public List<JobView> listJobs() {
		return port.listJobs();
	}

	@Override
	public void clearJobs() {
		port.clearJobs();
	}

}
