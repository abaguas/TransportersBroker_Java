package pt.upa.transporter.ws;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *  Unit DecideJobTest example
 *  
 *  Invoked by Maven in the "test" life-cycle phase
 *  If necessary, should invoke "mock" remote servers 
 */
public class DecideJobTest extends AbstractTransporterTest {

	private TransporterPort t1;
	
	@Override
	protected void populate() {
		t1 = new TransporterPort("UpaTransporterPort1");
		Job j1 = new Job("Lisboa", "Setúbal"); //id=20
		Job j2 = new Job("Lisboa", "Faro");
		j1.setCompanyName("UpaTransporter1");
		j2.setCompanyName("UpaTransporter1");
		j1.setPrice(15);
		j2.setPrice(75);
		t1.getAvailableJobs().add(j1);
		t1.getAvailableJobs().add(j2);
		t1.getRequestedJobs().add(j1);
		t1.getRequestedJobs().add(j2);
		
	}
	
	
    // tests

    @Test
    public void successAccept() throws BadJobFault_Exception  {
    	JobView jv  = t1.decideJob("20", true);
    	
    	assertNotNull("incorrect behaviour", jv);
    	assertEquals("incorrect final state", "ACCEPTED", jv.getJobState().value());
    }
    
    @Test
    public void successReject() throws BadJobFault_Exception  {
    	JobView jv  = t1.decideJob("25", false);
    	
    	assertNotNull("incorrect behaviour", jv);
    	assertEquals("incorrect final state", "REJECTED", jv.getJobState().value());
    }
    
    
    @Test(expected = BadJobFault_Exception.class) //invalid id
    public void decideInvalidID() throws BadJobFault_Exception  {
    	JobView jv  = t1.decideJob("30", true);
    }
    
}