package pt.upa.broker.ws;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.ca.ws.CA;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;

import javax.jws.WebService;
import javax.xml.registry.JAXRException;

@WebService(
	    endpointInterface="pt.upa.broker.ws.BrokerPortType",
	    wsdlLocation="broker.1_0.wsdl",
	    name="BrokerWebService",
	    portName="BrokerPort",
	    targetNamespace="http://ws.broker.upa.pt/",
	    serviceName="BrokerService"
)

public class BrokerPort implements BrokerPortType {
	
	private String id = "0";
	private String uddiURL;
	private String name = "UpaTransporter%";
	private Map<Transport, String> transports = new HashMap<Transport, String>();
	private CA ca;
	private Map<String, PublicKey> keys = new HashMap<String, PublicKey>();
	
	public BrokerPort (String uddiURL){
		this.uddiURL = uddiURL;
	}
	
	public Collection<String> list () throws JAXRException {
    	String uddiURL = getUddiURL();
    	String name = getName();
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
    	UDDINaming uddiNaming = new UDDINaming(uddiURL);
    	System.out.printf("Looking for '%s'%n", name);
        Collection<String> endpointAddress = uddiNaming.list(name);
       
        
        if (endpointAddress.isEmpty()) {
            System.out.println("Not found!");
            return null;
        } 
        else {
            Collection<UDDIRecord> record = uddiNaming.listRecords(name);
            
        	for (UDDIRecord rec : record){
        		String portName = rec.getOrgName();
        		if(!keys.containsKey(portName)){
//        			PublicKey pk = ca.getPublicKey(portName);
        			String pk = ca.getPublicKey(portName);
        			//keys.put(portName, pk);
        		}
        	}
            return endpointAddress;
        }
    }
	
	public String lookUp (String name) throws JAXRException {
		String uddiURL = getUddiURL();
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
	
	@Override
	public String ping(String name) {
		try {
			TransporterClient tc = null;
			Collection<String> list = list();
			for (String endpointURL: list){
				tc = new TransporterClient(endpointURL);
				if (tc.ping(name)==null){
					return null;
				}
			}
			return "OK";
		} catch (JAXRException e1) {
			return "Unreachable"; //TODO connection exception
		}
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {

		Collection<String> endpoints = null;
		Map<JobView, String> jobViews = new HashMap<JobView, String>();
		Transport t = new Transport(origin, destination); 
		TransporterClient tc = null;
		
		try {
			JobView jv = null;
			endpoints = list();
			for (String endpoint : endpoints){
				tc = new TransporterClient(endpoint);
				jv = tc.requestJob(origin, destination, price);
				if (jv!=null){
					jobViews.put(jv, endpoint);
				}
			}
		} catch (JAXRException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();			
		} catch (BadLocationFault_Exception e) {
			t.setState("FAILED");
			UnknownLocationFault ulf = new UnknownLocationFault();
			ulf.setLocation(e.getFaultInfo().getLocation());
			throw new UnknownLocationFault_Exception(e.getMessage(), ulf);
		
		} catch (BadPriceFault_Exception e) {
			t.setState("FAILED");
			InvalidPriceFault ipf = new InvalidPriceFault();
			ipf.setPrice(e.getFaultInfo().getPrice());
			throw new InvalidPriceFault_Exception(e.getMessage(), ipf);

		} 

		if(jobViews.isEmpty()){
			System.out.println("Entrei");
			t.setState("FAILED");
			UnavailableTransportFault utf = new UnavailableTransportFault();
			utf.setOrigin(origin);
			utf.setDestination(destination);
			throw new UnavailableTransportFault_Exception("Unavailable transport from origin to destination", utf);
		}
		else{
			return chooseJob(jobViews, price, t);
		}

	}
	
	public String chooseJob (Map<JobView, String> jobViews, int price, Transport t) throws UnavailableTransportPriceFault_Exception{
		Collection<JobView> jvs = jobViews.keySet();
		JobView budgetedJob = null;
		
		for (JobView j : jvs) {
			if (j.getJobPrice()<=price){
				budgetedJob = j;
				price = j.getJobPrice();
			}
		}
		
		if (budgetedJob == null) {
			t.setState("FAILED");
			t.setIdentifier(idFactory());
			UnavailableTransportPriceFault utpf = new UnavailableTransportPriceFault();
			utpf.setBestPriceFound(price);
			throw new UnavailableTransportPriceFault_Exception("Non-existent transport with pretended price",utpf);
		}
		
		t.setCompanyName(budgetedJob.getCompanyName());
		t.setPrice(budgetedJob.getJobPrice());
		t.setState("BUDGETED");
		t.setIdentifier(budgetedJob.getJobIdentifier());
        transports.put(t, jobViews.get(budgetedJob));
        
        decideJob(jvs, jobViews, budgetedJob, t);
        
        return t.getIdentifier();
	}
	
	public void decideJob(Collection<JobView> jvs, Map<JobView, String> jobViews, JobView budgetedJob, Transport t){
		TransporterClient tc = null;
		
		for (JobView j: jvs) {
			
			tc = new TransporterClient(jobViews.get(j));

			if (j.equals(budgetedJob)){
				try {
					tc.decideJob(t.getIdentifier(), true);
					t.setState("BOOKED");
				} catch (BadJobFault_Exception e) {
					t.setState("FAILED");
				}
			}
			else {
				try {
					tc.decideJob(t.getIdentifier()+idFactory(), false);
					t.setState("FAILED");
				} catch (BadJobFault_Exception e) {
					t.setState("FAILED");
				}
			}
			
		}
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		Transport transport = getTransportById(id);
		TransporterClient tc=null;	

		tc = new TransporterClient(transports.get(transport));
		
		JobView jv = tc.jobStatus(id);
		
		JobStateView jsv = jv.getJobState();
			
		String state = viewToState(jsv);
		if (!state.equals("ACCEPTED") && !state.equals("ACCEPTED")){
			transport.setState(state);	
		}
		return transport.createTransportView();
		
	}

	@Override
	public ArrayList<TransportView> listTransports() {
		ArrayList<TransportView> transportViews = new ArrayList<TransportView>();
		Collection<Transport> transps = transports.keySet();
		
		List<Transport> list = new ArrayList<Transport>(transps);
		
		for (Transport transport: list) {
	        try {
				transportViews.add(viewTransport(transport.getIdentifier()));
			} catch (UnknownTransportFault_Exception e) {
				System.out.println("Cosmic ray exception");
				//never happens because the transporter id came from the transports map
			}
	    }
		return transportViews;
	}

	@Override
	public void clearTransports() {
		TransporterClient tc = null;
		Collection<String> clientEndpoints = transports.values();
		
		for (String endpoint: clientEndpoints){
			tc = new TransporterClient(endpoint);
			tc.clearJobs();
		}

		transports.clear();
	}
    
	public Transport getTransportById(String id) throws UnknownTransportFault_Exception{
		if (id != null){
			Collection<Transport> transps = transports.keySet();
			for (Transport t: transps){
				if (id.equals(t.getIdentifier())){
					return t;
				}
			}
		}
		UnknownTransportFault fault = new UnknownTransportFault();
		fault.setId(id);
		throw new UnknownTransportFault_Exception("Unknown id", fault);
	}
	
	public String idFactory(){
		String id = getId();
		int i = Integer.parseInt(id);
		i++;
		
		id = Integer.toString(i);
		setId(id);
		
		return "f".concat(id);
	}
	
	public String viewToState(JobStateView view){
		return view.name();
	}
	
	public String getUddiURL() {
		return uddiURL;
	}

	public void setUddiURL(String uddiURL) {
		this.uddiURL = uddiURL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
