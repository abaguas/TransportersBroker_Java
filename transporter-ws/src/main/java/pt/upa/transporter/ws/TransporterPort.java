package pt.upa.transporter.ws;

import java.util.ArrayList;
import java.util.List;

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
	
	@Override
	public String ping(String name) {
		return "Pong";
	}

	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		
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
