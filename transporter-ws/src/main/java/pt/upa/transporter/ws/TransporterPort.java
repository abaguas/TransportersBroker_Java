package pt.upa.transporter.ws;

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

	@Override
	public String ping(String name) {
		return "Pong";
	}
	
	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
	//O PAR ORIGIN-DESTINATION E UNICO?
		if(price<0){
			BadPriceFault bpf = new BadPriceFault();
			bpf.setPrice(price);
			throw new BadPriceFault_Exception("The price must be positive", bpf);
		}
		if(!(origin.equals("Norte") || origin.equals("Centro") || origin.equals("Sul"))){
			BadLocationFault blf = new BadLocationFault();
			blf.setLocation(origin);
			throw new BadLocationFault_Exception("Unknown origin", blf);
		}
		if(!(destination.equals("Norte") || destination.equals("Centro") || destination.equals("Sul"))){
			BadLocationFault blf = new BadLocationFault();
			blf.setLocation(destination);
			throw new BadLocationFault_Exception("Unknown destination", blf);
		}
		
		JobView jv= getJobByRoute(origin, destination);
		
		if(price<100 && operate(origin,destination)){
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

	public boolean operate(String origin, String destination){
		//TODO where does the transporter operate?
		return true;
	}
	
	public JobView getJobByRoute(String origin, String destination){
		for (JobView j : jobs){
			if(origin==j.getJobOrigin() && destination==j.getJobDestination()){
				return j;
			}
		}
		return null;
	}
	

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobView jobStatus(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<JobView> listJobs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearJobs() {
		// TODO Auto-generated method stub
		
	}

}
