package pt.upa.transporter.ws;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.swing.Timer;
import pt.upa.transporter.exception.DoesNotOperateException;
import pt.upa.transporter.exception.InvalidIdentifierException;
import pt.upa.transporter.exception.NoAvailableIdentifierException;

@WebService(
	endpointInterface="pt.upa.transporter.ws.TransporterPortType",
	wsdlLocation="transporter.1_0.wsdl",
	name="TransporterWebService",
	portName="TransporterPort",
	targetNamespace="http://ws.transporter.upa.pt/",
	serviceName="TransporterService"
)

@HandlerChain(file = "/Transporter-chain.xml")
public class TransporterPort implements TransporterPortType{

	private String id;
	private String boundId;
	private String name;
	private String uddiURL;
	private ArrayList<Job> jobs = new ArrayList<Job>();
	private ArrayList<String> regiaoSul = new ArrayList<String>(
			Arrays.asList("Setúbal", "Évora", "Portalegre", "Beja", "Faro"));
	private ArrayList<String> regiaoCentro = new ArrayList<String>(
			Arrays.asList("Lisboa", "Leiria", "Santarém", "Castelo Branco", "Coimbra", "Aveiro", "Viseu", "Guarda"));
	private ArrayList<String> regiaoNorte = new ArrayList<String>(
			Arrays.asList("Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança"));

	
	public TransporterPort (String name, String uddiURL){
		this.name = name;
		int n = numberTransporter(name)*100000;
		id = Integer.toString(n);
		boundId = Integer.toString(n+100000);
		this.uddiURL = uddiURL;
	}
	
	@Override
	public String ping(String name) {
		return this.name;
		
	}
	
	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		
		if(origin==null || destination == null){
			BadLocationFault blf = new BadLocationFault();
			blf.setLocation(origin);
			throw new BadLocationFault_Exception("Unknown origin", blf);
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
		if(price < 0){
			BadPriceFault bpf = new BadPriceFault();
			bpf.setPrice(price);
			throw new BadPriceFault_Exception("The price must be positive", bpf);
		}
		
		try {
		  operate(origin, destination, getName());
		} catch (DoesNotOperateException dnoe){
			return null;
		}
		
		
		Random rand = new Random();
		
		int offer;
		
		if(price>100){
			return null;
		}
		if (price==0) {
			return null;
		}
		else if(price<=10){
			offer = rand.nextInt(price);
		}
		else if(price%2==numberTransporter(getName())%2){
			offer = rand.nextInt(price);
		}
		else{
			offer = rand.nextInt(100) + price + 1;
		}
		
		if (offer == 0){
			offer+=1;
		}
		
		Job newJob = new Job(getName(), idFactory(), origin, destination, offer); 
		
		addJob(newJob);
	
		return newJob.createJobView();
	}
	
	public int numberTransporter(String name){
		String number = name.substring(name.length()-1);
		return Integer.parseInt(number);
	}

	public void operate(String origin, String destination, String name) throws DoesNotOperateException {
		if (regiaoCentro.contains(origin) && regiaoCentro.contains(destination)){
		}
		else if ((regiaoNorte.contains(origin) || regiaoCentro.contains(origin)) && 
			((regiaoNorte.contains(destination) || regiaoCentro.contains(destination)))){
			if (numberTransporter(name)%2!=0){
				throw new DoesNotOperateException(name, origin, destination);
			}
		}
		else if ((regiaoSul.contains(origin) || regiaoCentro.contains(origin)) && 
			(regiaoSul.contains(destination) || regiaoCentro.contains(destination))){
			if (numberTransporter(name)%2!=1){
				throw new DoesNotOperateException(name, origin, destination);
			}
		}
		else{
			throw new DoesNotOperateException(name, origin, destination);
		}
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		Random rand = new Random();
		int delay;
		
		final Job j;
		if (id==null){
			BadJobFault fault = new BadJobFault();
			fault.setId(id);
			throw new BadJobFault_Exception("invalid ID", fault);
		}
		
		try{
			j = getJobById(id);
		}catch (InvalidIdentifierException iie){
			BadJobFault fault = new BadJobFault();
			fault.setId(id);
			throw new BadJobFault_Exception("invalid ID", fault);
		}

		if (j.getState()!="PROPOSED"){
			BadJobFault fault = new BadJobFault();
			fault.setId(id);
			throw new BadJobFault_Exception("invalid ID", fault);
		}
		
		if(!accept){
			j.setState("REJECTED");
		}
		
		else{
			delay = rand.nextInt(4000) + 1000;
			
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
		if (id==null){
			return null;
		}
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
		ArrayList<Job> jobs = getJobs();
		for (Job j : jobs) {
			jobViews.add(j.createJobView());
		}
		return jobViews;
	}

	@Override
	public void clearJobs() {
		getJobs().clear();
	}
	
	public Job getJobById(String id) throws InvalidIdentifierException {
		ArrayList<Job> jobs = getJobs();
		
		for (Job j: jobs){
			if (j.getIdentifier().equals(id)) {
				return j;
			}
		}
		throw new InvalidIdentifierException(id);
	}
	
	
	public void acceptedToHeading(Job j){
		int delay;
		Random rand = new Random();
		
		delay = rand.nextInt(4000) + 1000;
		
		j.setState("HEADING");
	    
		Timer timer = new Timer(delay, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	           headingToOngoing(j);
	        }
	    });
	    timer.start();
	}
	
	public void headingToOngoing(Job j){
		int delay;
		Random rand = new Random();
		
		delay = rand.nextInt(4000) + 1000;
		
		j.setState("ONGOING");
	    
		Timer timer = new Timer(delay, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	           ongoingToCompleted(j);
	        }
	    });
	    timer.start();
	}
	
	public void ongoingToCompleted(Job j){
		j.setState("COMPLETED");
	}
	
	
	public String idFactory(){
		String id = getId();
		int i = Integer.parseInt(id);
		i++;
		
		if (i > Integer.parseInt(getBoundId())){
			throw new NoAvailableIdentifierException(getName(), i);
		}
		
		setId(Integer.toString(i));
		return id;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUddiURL(){
		return uddiURL;
	}
	
	public ArrayList<Job> getJobs() {
		return this.jobs;
	}
	
	public void addJob(Job job) {
		this.jobs.add(job);
	}
	
	public void removeJob(Job job) {
		this.jobs.remove(job);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBoundId() {
		return boundId;
	}

	public void setBoundId(String boundId) {
		this.boundId = boundId;
	}
	
	


}
