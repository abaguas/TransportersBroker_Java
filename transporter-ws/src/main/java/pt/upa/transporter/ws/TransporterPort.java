package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.jws.WebService;

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
	private ArrayList<String> regiaoSul = new ArrayList<String>(
			Arrays.asList("Setúbal", "Évora", "Portalegre", "Beja", "Faro"));
	private ArrayList<String> regiaoCentro = new ArrayList<String>(
			Arrays.asList("Lisboa", "Leiria", "Santarém", "Castelo Branco", "Coimbra", "Aveiro", "Viseu", "Guarda"));
	private ArrayList<String> regiaoNorte = new ArrayList<String>(
			Arrays.asList("Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança"));
	
	@Override
	public String ping(String name) {
		return "Pong";
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
				offer = rand.nextInt(price) + 10;
			}
			
			else{
				offer = rand.nextInt(100) + price;
			}
			
			j.setPrice(offer);
			
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
		// TODO Auto-generated method stub
		Job j = getJobById(id);
		if(j == null)
		{
			BadJobFault fault = new BadJobFault();
			fault.setId(id);
			throw new BadJobFault_Exception("invalid ID", fault);
		}
			
		if(!accept)
			return null;
		else
		{
			j.setState(JobStateView.ACCEPTED);
			return j.createJobView();
		}
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

}
