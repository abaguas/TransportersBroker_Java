package pt.upa.broker.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import pt.upa.transporter.ws.BadLocationFault;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.transporter.ws.cli.TransporterClientException;

import java.util.Properties;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

import javax.jws.WebService;
import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

@WebService(
	    endpointInterface="pt.upa.broker.ws.BrokerPortType",
	    wsdlLocation="broker.1_0.wsdl",
	    name="BrokerWebService",
	    portName="BrokerPort",
	    targetNamespace="http://ws.broker.upa.pt/",
	    serviceName="BrokerService"
)

public class BrokerPort implements BrokerPortType {
	
	private String uddiURL;
	private String name = "UpaTransporter%";
	private ArrayList<TransportView> transporterViews = new ArrayList<TransportView>();
	
	public BrokerPort (String uddiURL){
		this.uddiURL = uddiURL;
	}
	
	public Collection<String> lookUp () throws JAXRException { //FIXME má prática?
    	String uddiURL = getUddiURL();
    	String name = getName();
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
    	UDDINaming uddiNaming = new UDDINaming(uddiURL);
    	System.out.printf("Looking for '%s'%n", name);
        Collection<String> endpointAddress = uddiNaming.list(name);
        
        if (endpointAddress == null) {
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
	    } catch (TransporterClientException e2) {
		    return "Unreachable";
	    }
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		Collection<String> endpoints;
		ArrayList<JobView> jobViews = new ArrayList<JobView>();
		try {
			endpoints = lookUp();
			for (String endpoint : endpoints){
				TransporterClient tc = new TransporterClient(endpoint);
				jobViews.add(tc.requestJob(origin, destination, price));
			}
		
		} catch (JAXRException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransporterClientException e3){
			e3.printStackTrace();			
		} catch (BadLocationFault_Exception e) {
			UnknownLocationFault ulf = new UnknownLocationFault();
			ulf.setLocation(e.getFaultInfo().getLocation());
			throw new UnknownLocationFault_Exception(e.getMessage(), ulf);
		} catch (BadPriceFault_Exception e) {
			InvalidPriceFault ipf = new InvalidPriceFault();
			ipf.setPrice(e.getFaultInfo().getPrice());
			throw new InvalidPriceFault_Exception(e.getMessage(), ipf);
		}
		
		if(jobViews.isEmpty()){
			UnavailableTransportFault utf = new UnavailableTransportFault();
			utf.setOrigin(origin);
			utf.setDestination(destination);
			throw new UnavailableTransportFault_Exception("Unavailable transport from origin to destination", utf);
		}
		else{
			return chooseJob(jobViews, price);				
		}
	}
	
	public String chooseJob (ArrayList<JobView> jobViews, int price){
		String id = null;
		
		for (JobView j : jobViews) {
			if (j.getJobPrice()<=price){
				
			}
		}
		if(jv.getJobPrice()>tv.getPrice()){
			UnavailableTransportPriceFault utpf = new UnavailableTransportPriceFault();
			utpf.setBestPriceFound(price);
			throw new UnavailableTransportPriceFault_Exception("Non-existent transport with pretended price",utpf);
		
		}

	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		UnknownTransportFault fault = new UnknownTransportFault();
		fault.setId(id);
		for(TransportView tv : transporterViews)
			if(tv.getId().equals(id))
				return tv;
		throw new UnknownTransportFault_Exception("Unknown id", fault);
	}

	@Override
	public List<TransportView> listTransports() {
		return transporterViews;
	}

	@Override
	public void clearTransports() { //FIXME se cliente falhar, apagar o transporterViews deve falhar tambem?
		for(TransportView tv : transporterViews) {
			transporterViews.remove(tv);
		try {
			TransporterClient client = new TransporterClient(getUddiURL(), "UPATranporter 1");
			client.clearJobs();
		} catch (TransporterClientException e) {
			e.printStackTrace();
		} //FIXME

		}
	}

	public TransportView getTransportById(String id){
		for (TransportView t: transporterViews){
			if (id==t.getId()){
				return t;
			}
		}
		return null;
	}
	// TODO

	
	
	
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

}
