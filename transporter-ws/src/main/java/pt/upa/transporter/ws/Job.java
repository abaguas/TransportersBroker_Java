package pt.upa.transporter.ws;

import pt.upa.transporter.exception.InvalidStateException;

public class Job {
	private String companyName;
	private String identifier;
	private String origin;
	private String destination;
	private int price;
	private String state;
	
	
	public Job(String name, String id, String origin, String destination, int price){
		this.companyName = name;
		this.identifier = id;
		this.origin = origin;
		this.destination = destination;
		this.price = price;
		this.state = "PROPOSED";
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
		return JobStateView.fromValue(state);
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