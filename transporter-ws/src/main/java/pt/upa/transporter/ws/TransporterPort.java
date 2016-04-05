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

	private ArrayList<JobView> jobs = new ArrayList<JobView>();
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
		
		JobView jv= getJobByRoute(origin, destination);
		
		if(price<100 && operate(origin,destination, jv.getCompanyName())){
			Random rand = new Random();
			int offer;
			
			if(price<=10){
				offer = rand.nextInt(9) + 0;
			}
			
			//FIXME getCompanyName ou getter do seu proprio nome
			else if(price%2==numberTransporter(jv.getCompanyName())%2){
				offer = rand.nextInt(price) + 10;
			}
			
			else{
				offer = rand.nextInt(100) + price;
			}
			
			jv.setJobPrice(offer);
			
			return jv;
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
	
	public JobView getJobByRoute(String origin, String destination){
		int min = 100;
		JobView best =null;
		for (JobView j : jobs){
			if(origin==j.getJobOrigin() && destination==j.getJobDestination()){
				if(j.getJobPrice()<min){
					best=j;
					min=j.getJobPrice();
				}
			}
		}
		return best;
	}
	

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		// TODO Auto-generated method stub
		JobView jv = getJobById(id);
		if(jv == null)
		{
			BadJobFault fault = new BadJobFault();
			fault.setId(id);
			throw new BadJobFault_Exception("invalid ID", fault);
		}
			
		if(!accept)
			return null;
		else
		{
			jv.setJobState(JobStateView.ACCEPTED);
			return jv;
		}
	}

	@Override
	public JobView jobStatus(String id) {
		return getJobById(id);
	}

	@Override
	public List<JobView> listJobs() {
		return jobs;
	}

	@Override
	public void clearJobs() {
		jobs.clear();
	}

	public JobView getJobById(String id){
		for (JobView j: jobs){
			if (id==j.getJobIdentifier()){
				return j;
			}
		}
		return null;
	}

}
