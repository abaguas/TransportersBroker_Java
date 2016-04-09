package pt.upa.broker.ws;

import pt.upa.broker.ws.TransportStateView;
import pt.upa.broker.ws.TransportView;
import pt.upa.transporter.ws.JobView;

public class Transport {
	
	private String companyName;
	private String identifier;
	private String origin;
	private String destination;
	private int price;
	private TransportStateView state;

	public Transport(JobView jv, String id, int price) {
		this.companyName = jv.getCompanyName();
		this.identifier = id;
		this.origin = jv.getJobOrigin();
		this.destination = jv.getJobDestination();
		this.price = price;
		state = TransportStateView.REQUESTED;
	}
	
	public TransportView createTransportView() {
		TransportView tv = new TransportView();
		tv.setTransporterCompany(companyName);
		tv.setId(identifier);
		tv.setOrigin(origin);
		tv.setDestination(destination);
		tv.setPrice(price);
		tv.setState(state);
		return tv;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public TransportStateView getState() {
		return state;
	}

	public void setState(TransportStateView state) {
		this.state = state;
	}
}
