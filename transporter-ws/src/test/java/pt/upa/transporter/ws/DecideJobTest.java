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
	
	@Override
	protected void populate() {
	}
	
	
    // tests

    @Test
    public void successAccept() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = port1.requestJob("Lisboa", "Faro", 40);
    	JobView jview = port1.decideJob(jv.getJobIdentifier(), true);
    	
    	assertNotNull("incorrect behaviour", jview);
    	assertEquals("incorrect final state", "ACCEPTED", jview.getJobState().value());
    }
    
    @Test
    public void successReject() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception  {
    	JobView jv = port1.requestJob("Lisboa", "Faro", 40);
    	JobView jview  = port1.decideJob(jv.getJobIdentifier(), false);
    	
    	assertNotNull("incorrect behaviour", jview);
    	assertEquals("incorrect final state", "REJECTED", jview.getJobState().value());
    }
    
    @Test//invalid state
    public void stateNotProposed() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception  {
    	JobView jv = port1.requestJob("Lisboa", "Faro", 40);
    	jv.setJobState(JobStateView.ACCEPTED);
    	JobView jview  = port1.decideJob(jv.getJobIdentifier(), false);
    	
    	assertNull("cant accept already accepted", jview);
    }
    
    @Test(expected = BadJobFault_Exception.class) //invalid id
    public void decideInvalidID() throws BadJobFault_Exception  {
    	port1.decideJob("ahgll", true);
    }
    
}