package pt.upa.broker.ws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

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
	
	private int id = 1;
	private String uddiURL;
	private String name = "UpaTransporter%";
	private ArrayList<Transport> transports = new ArrayList<Transport>();
	
	public BrokerPort (String uddiURL){
		this.uddiURL = uddiURL;
	}
	
	public Collection<String> list () throws JAXRException { //FIXME má prática?
    	String uddiURL = getUddiURL();
    	String name = getName();
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
    	UDDINaming uddiNaming = new UDDINaming(uddiURL);
    	System.out.printf("Looking for '%s'%n", name);
        Collection<String> endpointAddress = uddiNaming.list(name);
        
        if (endpointAddress.isEmpty()) {
            System.out.println("Not found!");
            return null;
        } else {
            return endpointAddress;
        }
    }
	
	public String lookUp (String name) throws JAXRException { //FIXME má prática?
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
			String endpointURL = lookUp(name);
			TransporterClient tc = new TransporterClient(endpointURL); //completar com UDDIURL
			return tc.ping(name);
		} catch (JAXRException e1) {
			return "Unreachable";
		}
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {

		Collection<String> endpoints = null;
		Map<JobView, TransporterClient> jobViews = new HashMap<JobView, TransporterClient>();
		Transport t = new Transport(idFactory(), origin, destination); 
		TransporterClient tc = null;
		
		try {
			endpoints = list();
			for (String endpoint : endpoints){
				tc = new TransporterClient(endpoint);
				jobViews.put(tc.requestJob(origin, destination, price), tc);
			}
			if(jobViews.isEmpty()){   //FIXME se tiver nulls dá empty?
				t.setState("FAILED");
				UnavailableTransportFault utf = new UnavailableTransportFault();
				utf.setOrigin(origin);
				utf.setDestination(destination);
				throw new UnavailableTransportFault_Exception("Unavailable transport from origin to destination", utf);
			}
			else{
				return chooseJob(jobViews, price, t);
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
		return null; // Never gets here
	}
	
	public String chooseJob (Map<JobView, TransporterClient> jobViews, int price, Transport t) throws UnavailableTransportPriceFault_Exception{
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
			UnavailableTransportPriceFault utpf = new UnavailableTransportPriceFault();
			utpf.setBestPriceFound(price);
			throw new UnavailableTransportPriceFault_Exception("Non-existent transport with pretended price",utpf);
		}
		
		t.setCompanyName(budgetedJob.getCompanyName());
		t.setPrice(budgetedJob.getJobPrice());
		t.setState("BUDGETED");
        transports.add(t);
        
        decideJob(jvs, jobViews, budgetedJob, t);
        
        return t.getIdentifier();
	}
	
	public void decideJob(Collection<JobView> jvs, Map<JobView, TransporterClient> jobViews, JobView budgetedJob, Transport t){
		TransporterClient tc = null;
		
		for (JobView j: jvs) {
			tc = jobViews.get(j);
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
					tc.decideJob(t.getIdentifier(), false);
					t.setState("FAILED");
				} catch (BadJobFault_Exception e) {
					t.setState("FAILED");
				}
			}
			
		}
	}

	//FIXME atualizar o job status
	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		for(Transport t : transports) {
			if(t.getIdentifier().equals(id)) {
				return t.createTransportView();
			}
		}
		UnknownTransportFault fault = new UnknownTransportFault();
		fault.setId(id);
		throw new UnknownTransportFault_Exception("Unknown id", fault);
	}

	@Override
	public ArrayList<TransportView> listTransports() {
		ArrayList<TransportView> transportViews = new ArrayList<TransportView>();
		for (Transport t : transports) {
			transportViews.add(t.createTransportView());
		}
		return transportViews;
	}

	@Override
	public void clearTransports() { //FIXME se cliente falhar, apagar o transporterViews deve falhar tambem?
		removeTransports();
		Collection<String> endpoints = null;
		try {
			endpoints = list();
			for (String endpoint : endpoints){
				TransporterClient tc = new TransporterClient(endpoint);
				tc.clearJobs();
			}
		} catch (JAXRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public void removeTransports() {
    	for (Transport t : transports) {
    		transports.remove(t);
    	}
    }
    
	public Transport getTransportById(String id){
		for (Transport t: transports){
			if (id==t.getIdentifier()){
				return t;
			}
		}
		return null;
	}
	
	public int idFactory(){
		int id = getId();
		setId(id+1);
		
		return id;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
