package pt.upa.transporter.ws;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 *  Unit JobStatusTest
 *  
 *  Invoked by Maven in the "test" life-cycle phase
 *  If necessary, should invoke "mock" remote servers 
 */
public class JobStatusTest extends AbstractTransporterTest {

	/* TESTES:
	 * * JobStatus with valid identifier
	 * * JobStatus with invalid identifier
	 * * JobStatus with null identifier
	 */
	
	@Override
	protected void populate() {
		
	}
	
    // tests

	@Test
    public void proposedIdentifierTest() throws BadLocationFault_Exception, BadPriceFault_Exception {
		JobView jv = port2.requestJob("Porto", "Guarda", 40);
		JobView jview = port2.jobStatus(jv.getJobIdentifier());
		
    	assertNotNull("jobView does not exist", jview);
    	assertEquals("incorrect state", JobStateView.PROPOSED, jv.getJobState());
    }
	
	@Test
    public void acceptedIdentifierTest() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
		JobView jv = port2.requestJob("Porto", "Guarda", 40);
		JobView j = port2.decideJob(jv.getJobIdentifier(), true);
		JobView jview = port2.jobStatus(j.getJobIdentifier());
		
    	assertNotNull("jobView does not exist", jview);
    	assertEquals("incorrect state", JobStateView.ACCEPTED, jview.getJobState());
    }
	
	@Test
    public void rejectedIdentifierTest() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
		JobView jv = port2.requestJob("Porto", "Guarda", 40);
		JobView j = port2.decideJob(jv.getJobIdentifier(), false);
		JobView jview = port2.jobStatus(j.getJobIdentifier());
		
    	assertNotNull("jobView does not exist", jview);
    	assertEquals("incorrect state", JobStateView.REJECTED, jview.getJobState());
    }

	@Test
    public void completedIdentifierTest() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
		JobView jv = port2.requestJob("Porto", "Guarda", 40);
		port2.decideJob(jv.getJobIdentifier(), true);
		JobView jview = port2.jobStatus(jv.getJobIdentifier());
		try {
		    Thread.sleep(15000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		}
		
    	assertNotNull("jobView does not exist", jview);
    	assertEquals("incorrect state", JobStateView.COMPLETED, jv.getJobState());
    }
	
	@Test
    public void notAcceptedIdentifierTest() throws BadLocationFault_Exception, BadPriceFault_Exception, BadJobFault_Exception {
		JobView jv = port2.requestJob("Porto", "Guarda", 40);
		port2.decideJob(jv.getJobIdentifier(), false);
		JobView jview = port2.jobStatus(jv.getJobIdentifier());
		try {
		    Thread.sleep(5000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		}
		
    	assertNotNull("jobView does not exist", jview);
    	assertNotEquals("incorrect state", JobStateView.ACCEPTED, jv.getJobState());
    }

	
	
	@Test
    public void invalidIdentifierTest() {
    	JobView jv = port2.jobStatus("sistemas distribuidos");
    	
    	assertNull("jobView does not exist", jv);
    }
	
	@Test
    public void nullIdentifierTest() {
    	JobView jv = port2.jobStatus(null);
    	
    	assertNull("jobView does not exist", jv);
    }

}