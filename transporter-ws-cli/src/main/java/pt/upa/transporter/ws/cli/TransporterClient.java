package pt.upa.transporter.ws.cli;

import java.util.Collection;
import java.util.List;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;
import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

public class TransporterClient implements TransporterPortType {	
	
	private TransporterPortType port;

    public TransporterClient(String endpointURL) throws TransporterClientException {
        createStub(endpointURL);
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
		return port.ping(name);
	}//é preciso apanhar aqui uma exceçao?

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		port.requestJob(origin, destination, price);
		return null;
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		return port.decideJob(id, accept);
	}

	@Override
	public JobView jobStatus(String id) {
		return port.jobStatus(id);
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
