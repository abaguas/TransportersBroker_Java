package pt.upa.transporter.ws;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.jws.WebService;
import javax.swing.Timer;

@WebService(
	endpointInterface="pt.upa.transporter.ws.TransporterPortType",
	wsdlLocation="transporter.1_0.wsdl",
	name="TransporterWebService",
	portName="TransporterPort",
	targetNamespace="http://ws.transporter.upa.pt/",
	serviceName="TransporterService"
)
public class TransporterPort implements TransporterPortType{

	private ArrayList<Job> jobs = new ArrayList<Job>();
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
		return name;
	}
	
	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
	//FIXME O PAR ORIGIN-DESTINATION E UNICO?
		if(price<0){
			BadPriceFault bpf = new BadPriceFault();
			bpf.setPrice(price);
			throw new BadPriceFault_Exception("The price must be positive", bpf);
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
		
		Job j = getJobByRoute(origin, destination);
		
		if(price<100 && operate(origin,destination, j.getCompanyName())){
			Random rand = new Random();
			int offer;
			
			if(price<=10){
				offer = rand.nextInt(9) + 0;
			}
			
			//FIXME getCompanyName ou getter do seu proprio nome
			else if(price%2==numberTransporter(j.getCompanyName())%2){
				offer = rand.nextInt(price) + 10; //FIXME limits ok?
			}
			
			else{
				offer = rand.nextInt(100) + price; //FIXME limits ok?
			}
			
			j.setPrice(offer);
			j.setCompanyName(getName());
			
			return j.createJobView();
		}
		else{
			return null;
		}
	}
	
	public int numberTransporter(String name){
		String number = name.substring(name.length() - 1);
		return Integer.parseInt(number);
	}

	public boolean operate(String origin, String destination, String name){
		if ((regiaoNorte.contains(origin) || regiaoCentro.contains(origin)) && 
			((regiaoNorte.contains(destination) || regiaoCentro.contains(destination)))){
			if (numberTransporter(name)%2==0){
				return true;
			}
			else return false;
		}
		else if ((regiaoSul.contains(origin) || regiaoCentro.contains(origin)) && 
			((regiaoSul.contains(destination) || regiaoCentro.contains(destination)))){
			if (numberTransporter(name)%2==1){
				return true;
			}
		}
		return false;
	}
	
	public Job getJobByRoute(String origin, String destination){
		int min = 100;
		Job best =null;
		for (Job j : jobs){
			if(origin==j.getOrigin() && destination==j.getDestination()){
				if(j.getPrice()<min){
					best=j;
					min=j.getPrice();
				}
			}
		}
		return best;
	}
	

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		Random rand = new Random();
		int delay;
		Job j = getJobById(id);
		
		if(j == null){
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
		    
			Timer timer = new Timer(delay, new ActionListener() { //FIXME milliseconds?
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
		return getJobById(id).createJobView();
	}

	@Override
	public List<JobView> listJobs() {
		ArrayList<JobView> jobViews = new ArrayList<JobView>();
		for (Job j : jobs) {
			jobViews.add(j.createJobView());
		}
		return jobViews;
	}

	@Override
	public void clearJobs() {
		jobs.clear();
	}

	public Job getJobById(String id) {
		for (Job j: jobs){
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
		return jobs;
	}

	public void addJob(Job job) {
		this.jobs.add(job);
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
