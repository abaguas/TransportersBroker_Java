package pt.upa.broker.ws;

import java.io.IOException;
import java.util.ArrayList;
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
	
	private ArrayList<TransportView> transporterViews = new ArrayList<TransportView>();
	
	public String getUrlUDDI () {
		java.io.InputStream is = this.getClass().getResourceAsStream("my.properties");
		java.util.Properties p = new Properties();
		try {
			p.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return p.getProperty("uddi.url");
	}
	
	@Override
	public String ping(String name) {
		try {
			TransporterClient tc = new TransporterClient(getUrlUDDI(), name); //completar com UDDIURL
			tc.ping(name);
		} catch (TransporterClientException e) {
			// TODO Auto-generated catch block
			//FIXME como lidamos com esta excecao?
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		
		TransporterClient tc=null;
		JobView jv =null;
		
		try {
			tc = new TransporterClient(getUrlUDDI(), "UPATransporter1");//FIXME nome do transporter
		} catch (TransporterClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		try {
			jv=tc.requestJob(origin, destination, price);
			
			
		} catch (BadLocationFault_Exception e) {
			UnknownLocationFault ulf = new UnknownLocationFault();
			ulf.setLocation(e.getFaultInfo().getLocation());
			throw new UnknownLocationFault_Exception(e.getMessage(), ulf);
			
		} catch (BadPriceFault_Exception e) {
			InvalidPriceFault ipf = new InvalidPriceFault();
			ipf.setPrice(e.getFaultInfo().getPrice());
			throw new InvalidPriceFault_Exception(e.getMessage(), ipf);
		}
		
		if(jv==null){
			UnavailableTransportFault utf = new UnavailableTransportFault();
			utf.setOrigin(origin);
			utf.setDestination(destination);
			throw new UnavailableTransportFault_Exception("Unavailable transport from origin to destination", utf);
		}
		else{
			
			String id = jv.getJobIdentifier();
			TransportView tv = getTransportById(id); //FIXME Create a new TransportView or get it by ID
			
			if(jv.getJobPrice()>tv.getPrice()){
				UnavailableTransportPriceFault utpf = new UnavailableTransportPriceFault();
				utpf.setBestPriceFound(price);
				throw new UnavailableTransportPriceFault_Exception("Non-existent transport with pretended price",utpf);
			
			}
				
		}
		
		return null; //FIXME return id
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
			TransporterClient client = new TransporterClient(getUrlUDDI(), "UPATranporter 1");
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

}
