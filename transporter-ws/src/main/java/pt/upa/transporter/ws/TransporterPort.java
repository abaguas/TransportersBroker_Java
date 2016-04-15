package pt.upa.transporter.ws;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.jws.WebService;
import javax.swing.Timer;

import pt.upa.transporter.exception.DoesNotOperateException;
import pt.upa.transporter.exception.InvalidIdentifierException;
import pt.upa.transporter.exception.NoJobsAvailableException;

@WebService(
	endpointInterface="pt.upa.transporter.ws.TransporterPortType",
	wsdlLocation="transporter.1_0.wsdl",
	name="TransporterWebService",
	portName="TransporterPort",
	targetNamespace="http://ws.transporter.upa.pt/",
	serviceName="TransporterService"
)
public class TransporterPort implements TransporterPortType{

	private ArrayList<Job> availableJobs = new ArrayList<Job>();
	private ArrayList<Job> requestedJobs = new ArrayList<Job>();
	private String name;
	private ArrayList<String> regiaoSul = new ArrayList<String>(
			Arrays.asList("Setúbal", "Évora", "Portalegre", "Beja", "Faro"));
	private ArrayList<String> regiaoCentro = new ArrayList<String>(
			Arrays.asList("Lisboa", "Leiria", "Santarém", "Castelo Branco", "Coimbra", "Aveiro", "Viseu", "Guarda"));
	private ArrayList<String> regiaoNorte = new ArrayList<String>(
			Arrays.asList("Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança"));
	
	public TransporterPort (String name){
		this.name = name;
	}
	
	@Override
	public String ping(String name) {
		return this.name;
	}
	
	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		Job j = null;
		if(price<0){
			BadPriceFault bpf = new BadPriceFault();
			bpf.setPrice(price);
			throw new BadPriceFault_Exception("The price must be positive", bpf);
		}
		if(price>100){
			return null;
		}
		if(!(regiaoNorte.contains(origin) || regiaoCentro.contains(origin) || regiaoSul.contains(origin))){
			BadLocationFault blf = new BadLocationFault();
			blf.setLocation(origin);
			throw new BadLocationFault_Exception("Unknown origin", blf);
		}
		if(!(regiaoNorte.contains(destination) || regiaoCentro.contains(destination) || regiaoSul.contains(destination))){
			BadLocationFault blf = new BadLocationFault();
			blf.setLocation(destination);
			throw new BadLocationFault_Exception("Unknown destination", blf);
		}
		System.out.println("antes do operate");
		try {
		  operate(origin, destination, getName());
		} catch (DoesNotOperateException dnoe){
			return null;
		}
		System.out.println("antes do get");
		try {
		  j = getJobByRoute(origin, destination);
		} catch (NoJobsAvailableException njae){
			return null;
		}
		System.out.println("antes do random");
		Random rand = new Random();
		int offer;
		if (price==0){
			return null;
		}
		else if(price<=10){
			offer = rand.nextInt(price);
		}
		else if(price%2==numberTransporter(getName())%2){
			offer = rand.nextInt(price); 
		}
		else{
			offer = rand.nextInt(100) + price; 
		}
		
		j.setPrice(offer);
		j.setCompanyName(getName());
		removeAvailableJob(j);
		addRequestedJob(j);
			
		return j.createJobView();
	}
	
	public int numberTransporter(String name){
		String number = name.substring(name.length()-1);
		return Integer.parseInt(number);
	}

	public void operate(String origin, String destination, String name) throws DoesNotOperateException {
		if ((regiaoNorte.contains(origin) || regiaoCentro.contains(origin)) && 
			((regiaoNorte.contains(destination) || regiaoCentro.contains(destination)))){
			if (numberTransporter(name)%2!=0){
				throw new DoesNotOperateException(name, origin, destination);
			}
		}
		else if ((regiaoSul.contains(origin) || regiaoCentro.contains(origin)) && 
			((regiaoSul.contains(destination) || regiaoCentro.contains(destination)))){
			if (numberTransporter(name)%2!=1){
				throw new DoesNotOperateException(name, origin, destination);
			}
		}
	}
	
	public Job getJobByRoute(String origin, String destination) throws NoJobsAvailableException{
		 ArrayList<Job> jbs = getJobs();
		for (Job j : jbs){
			if ((j.getOrigin().equals(origin)) && (j.getDestination().equals(destination))){
				return j;
			}
		}
		throw new NoJobsAvailableException(getName(), origin, destination);
	}
	

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		Random rand = new Random();
		int delay;
		//Job j = getJobById(id);
		Job j = getRequestedJobById(id);
		
		if(j == null){
			BadJobFault fault = new BadJobFault();
			fault.setId(id);
			throw new BadJobFault_Exception("invalid ID", fault);
		}
			
		if(!accept){
			j.setState("REJECTED");
			Job job = new Job(j);
			addAvailableJob(job);
		}
		
		else{
			delay = rand.nextInt(4000) + 1000; //FIXME milliseconds?
			
			j.setState("ACCEPTED");
		    
			Timer timer = new Timer(delay, new ActionListener() { 
		        @Override
		        public void actionPerformed(ActionEvent ae) {
		           acceptedToHeading(j);
		        }
		    });
		    timer.start();
		}
		
		return j.createJobView();
	}

	@Override
	public JobView jobStatus(String id) {
		try {
			Job j = getJobById(id);
			return j.createJobView();
		}catch (InvalidIdentifierException iie){
			return null;
		}
	}

	@Override
	public List<JobView> listJobs() {
		ArrayList<JobView> jobViews = new ArrayList<JobView>();
		for (Job j : availableJobs) {
			jobViews.add(j.createJobView());
		}
		return jobViews;
	}

	@Override
	public void clearJobs() {
		availableJobs.clear();
		requestedJobs.clear();
	}

	public List<JobView> listRequestedJobs() {
		ArrayList<JobView> jobViews = new ArrayList<JobView>();
		for (Job j : requestedJobs) {
			jobViews.add(j.createJobView());
		}
		return jobViews;
	}
	
	
	public Job getJobById(String id) throws InvalidIdentifierException {
		for (Job j: availableJobs){
			if (id == j.getIdentifier()) {
				return j;
			}
		}
		throw new InvalidIdentifierException(id);
	}
	
	public Job getRequestedJobById(String id) {
		for (Job j: requestedJobs){
			if (id==j.getIdentifier())
			{
				return j;
			}
		}
		return null;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Job> getJobs() {
		return availableJobs;
	}
	
	public ArrayList<Job> getRequestedJobs() {
		return requestedJobs;
	}
	
	public void addRequestedJob(Job job) {
		this.requestedJobs.add(job);
	}
	
	public void addAvailableJob(Job job) {
		this.availableJobs.add(job);
	}
	
	public void removeAvailableJob(Job job) {
		this.availableJobs.remove(job);
	}
	
	public void acceptedToHeading(Job j){
		int delay;
		Random rand = new Random();
		
		delay = rand.nextInt(4000) + 1000;
		
		j.setState("HEADING");
	    
		Timer timer = new Timer(delay, new ActionListener() { //FIXME milliseconds?
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	           acceptedToHeading(j);
	        }
	    });
	    timer.start();
	}
	
	public void headingToOngoing(Job j){
		int delay;
		Random rand = new Random();
		
		delay = rand.nextInt(4000) + 1000;
		
		j.setState("ONGOING");
	    
		Timer timer = new Timer(delay, new ActionListener() { //FIXME milliseconds?
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	           acceptedToHeading(j);
	        }
	    });
	    timer.start();
	}
	
	public void ongoingToCompleted(Job j){
		j.setState("COMPLETED");
	}



}
