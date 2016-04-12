package pt.upa.broker.ws;

import java.util.Iterator;

import pt.upa.broker.exception.InvalidStateException;
import pt.upa.broker.ws.TransportStateView;
import pt.upa.broker.ws.TransportView;
import pt.upa.transporter.ws.JobView;

public class Transport {
	
	private String companyName;
	private String identifier;
	private String origin;
	private String destination;
	private int price;
	private String state;

	public Transport(int id, String origin, String destination) {
		identifier = Integer.toString(id);
		this.origin = origin;
		this.destination = destination;
		this.state = "REQUESTED";
	}
	
	public TransportView createTransportView() {
		TransportView tv = new TransportView();
		tv.setTransporterCompany(companyName);
		tv.setId(identifier);
		tv.setOrigin(origin);
		tv.setDestination(destination);
		tv.setPrice(price);
		tv.setState(stateToView());
		return tv;
	}
	
	public TransportStateView stateToView(){
		if (state.equals("REQUEST")) {
			return TransportStateView.REQUESTED;
		}
		else if (state.equals("BUDGETED")) {
			return TransportStateView.BUDGETED;
		}
		else if (state.equals("BOOKED")) {
			return TransportStateView.BOOKED;
		}
		else if (state.equals("FAILED")) {
			return TransportStateView.FAILED;
		}
		else if (state.equals("HEADING")) {
			return TransportStateView.HEADING;
		}
		else if (state.equals("ONGOING")) {
			return TransportStateView.ONGOING;
		}
		else if (state.equals("COMPLETED")) {
			return TransportStateView.COMPLETED;
		}
		else {
			throw new InvalidStateException(state);
		}
		
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	
}
