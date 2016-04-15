package pt.upa.transporter.ws.it;

import org.junit.*;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;

import static org.junit.Assert.*;

public class DecideJobIT extends AbstractIT {

	@Override
	protected void populate() {		
	}
	
	@Test
    public void successAccept() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = tOdd.requestJob("Lisboa", "Faro", 40);
    	JobView jview = tOdd.decideJob(jv.getJobIdentifier(), true);
    	
    	assertNotNull("incorrect behaviour", jview);
    	assertEquals("incorrect final state", "ACCEPTED", jview.getJobState().value());
    }
    
    @Test
    public void successReject() throws BadJobFault_Exception, BadLocationFault_Exception, BadPriceFault_Exception  {
    	JobView jv = tOdd.requestJob("Lisboa", "Faro", 40);
    	JobView jview  = tOdd.decideJob(jv.getJobIdentifier(), false);
    	
    	assertNotNull("incorrect behaviour", jview);
    	assertEquals("incorrect final state", "REJECTED", jview.getJobState().value());
    }
    
    @Test(expected = BadJobFault_Exception.class) //invalid id
    public void decideInvalidID() throws BadJobFault_Exception  {
    	tOdd.decideJob("ahgll", true);
    }
    
}
