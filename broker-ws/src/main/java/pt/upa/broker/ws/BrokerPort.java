package pt.upa.broker.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
			e.printStackTrace();
		}
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

	// TODO

}
