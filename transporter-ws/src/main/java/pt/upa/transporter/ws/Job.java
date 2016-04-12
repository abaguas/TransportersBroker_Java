package pt.upa.transporter.ws;

import pt.upa.transporter.exception.InvalidStateException;

public class Job {
	private String companyName;
	private String identifier;
	private String origin;
	private String destination;
	private int price;
	private String state;
	
	public Job(String identifier, String origin, String destination) {
		this.companyName = "";
		this.identifier = identifier;
		this.origin = origin;
		this.destination = destination;
		this.price = -1;
		this.state="PROPOSED";
	}
	
	public JobView createJobView() {
		JobView jv = new JobView();
		jv.setCompanyName(companyName);
		jv.setJobIdentifier(identifier);
		jv.setJobOrigin(origin);
		jv.setJobDestination(destination);
		jv.setJobPrice(price);
		jv.setJobState(stateToView());
		return jv;
	}
	
	public JobStateView stateToView(){
		if (state.equals("PROPOSED")) {
			return JobStateView.PROPOSED;
		}
		else if (state.equals("ACCEPTED")) {
			return JobStateView.ACCEPTED;
		}
		else if (state.equals("REJECTED")) {
			return JobStateView.REJECTED;
		}
		else if (state.equals("HEADING")) {
			return JobStateView.HEADING;
		}
		else if (state.equals("ONGOING")) {
			return JobStateView.ONGOING;
		}
		else if (state.equals("COMPLETED")) {
			return JobStateView.COMPLETED;
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