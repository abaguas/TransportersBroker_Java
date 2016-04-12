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

	TransporterPort t1 = null;
	
	/* TESTES:
	 * * JobStatus with valid identifier
	 * * JobStatus with invalid identifier
	 * * JobStatus with null identifier
	 */
	
	@Override
	protected void populate() {
		t1 = new TransporterPort("UpaTransporter1");

		Job job = new Job("598", "Faro", "Beja");
		job.setPrice(40);
		job.setCompanyName("UpaTransporter1");
		
		t1.addJob(job);
		t1.addRequestedJob(job);		
	}
	
    // tests

	@Test
    public void validIdentifierTest() {
    	JobView jv = t1.jobStatus("598");
    	
    	assertNotNull("jobView does not exist", jv);
    	
    	assertEquals("incorrect company name", "UpaTransporter1", jv.getCompanyName());
    	assertEquals("incorrect job identifier", "1", jv.getJobIdentifier());    	
    	assertEquals("incorrect origin", "Faro", jv.getJobOrigin());
    	assertEquals("incorrect destination", "Beja", jv.getJobDestination());
    	assertEquals("incorrect price", 40, jv.getJobPrice());
    	assertEquals("incorrect state", "PROPOSED", jv.getJobState());
    }
	
	@Test
    public void invalidIdentifierTest() {
    	JobView jv = t1.jobStatus("sistemas distribuidos");
    	
    	assertNull("jobView does not exist", jv);
    }
	
	@Test
    public void nullIdentifierTest() {
    	JobView jv = t1.jobStatus(null);
    	
    	assertNull("jobView does not exist", jv);
    }


}