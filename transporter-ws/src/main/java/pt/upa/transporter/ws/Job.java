package pt.upa.transporter.ws;

public class Job {
	private String companyName;
	private String identifier;
	private String origin;
	private String destination;
	private int price;
	private JobStateView state;
	
	public Job(String companyName, String identifier, String origin, String destination, int price) {
		this.companyName = companyName;
		this.identifier = identifier;
		this.origin = origin;
		this.destination = destination;
		this.price = price;
		state = JobStateView.PROPOSED;
	}
	
	public JobStateView getJobStateView() {
		return state;
	}
	
	public JobView createJobView() {
		JobView jv = new JobView();
		jv.setCompanyName(companyName);
		jv.setJobIdentifier(identifier);
		jv.setJobOrigin(origin);
		jv.setJobDestination(destination);
		jv.setJobPrice(price);
		jv.setJobState(state);
		return jv;
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

	public JobStateView getState() {
		return state;
	}

	public void setState(JobStateView state) {
		this.state = state;
	}
	
	
	
}