package pt.upa.transporter.ws.it;

import org.junit.*;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;

import static org.junit.Assert.*;

public class JobStatusIT extends AbstractIT {

	@Override
	protected void populate() {		
	}
	
	@Test
    public void proposedIdentifierTest() throws BadLocationFault_Exception, BadPriceFault_Exception {
		JobView jv = tEven.requestJob("Porto", "Guarda", 40);
		JobView jview = tEven.jobStatus(jv.getJobIdentifier());
		
    	assertNotNull("jobView does not exist", jview);
    	assertEquals("incorrect state", JobStateView.PROPOSED, jv.getJobState());
    }
	
	@Test
    public void acceptedIdentifierTest() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
		JobView jv = tEven.requestJob("Porto", "Guarda", 40);
		JobView j = tEven.decideJob(jv.getJobIdentifier(), true);
		JobView jview = tEven.jobStatus(j.getJobIdentifier());
		
    	assertNotNull("jobView does not exist", jview);
    	assertEquals("incorrect state", JobStateView.ACCEPTED, jview.getJobState());
    }
	
	@Test
    public void rejectedIdentifierTest() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
		JobView jv = tEven.requestJob("Porto", "Guarda", 40);
		JobView j = tEven.decideJob(jv.getJobIdentifier(), false);
		JobView jview = tEven.jobStatus(j.getJobIdentifier());
		
    	assertNotNull("jobView does not exist", jview);
    	assertEquals("incorrect state", JobStateView.REJECTED, jview.getJobState());
    }

	@Test
    public void completedIdentifierTest() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
		JobView jv = tEven.requestJob("Porto", "Guarda", 40);
		JobView j = tEven.decideJob(jv.getJobIdentifier(), true);
		try {
		    Thread.sleep(25000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		}
		JobView jview = tEven.jobStatus(j.getJobIdentifier());
		
    	assertNotNull("jobView does not exist", jview);
    	assertEquals("incorrect state", JobStateView.COMPLETED, jview.getJobState());
    }

	@Test
    public void notAcceptedIdentifierTest() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
		JobView jv = tEven.requestJob("Porto", "Guarda", 40);
		tEven.decideJob(jv.getJobIdentifier(), false);
		JobView jview = tEven.jobStatus(jv.getJobIdentifier());
		try {
		    Thread.sleep(5000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		}
		
    	assertNotNull("jobView does not exist", jview);
    	assertNotEquals("incorrect state", JobStateView.ACCEPTED, jv.getJobState());
    }

	
	
	@Test
    public void invalidIdentifierTest() {
    	JobView jv = tEven.jobStatus("sistemas distribuidos");
    	
    	assertNull("jobView does not exist", jv);
    }
	
	@Test
    public void nullIdentifierTest() {
    	JobView jv = tEven.jobStatus(null);
    	
    	assertNull("jobView does not exist", jv);
    }


}
