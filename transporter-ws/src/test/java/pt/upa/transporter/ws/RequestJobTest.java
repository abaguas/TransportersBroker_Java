package pt.upa.transporter.ws;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *  Unit RequestJobTest
 *  
 *  Invoked by Maven in the "test" life-cycle phase
 *  If necessary, should invoke "mock" remote servers 
 */
public class RequestJobTest extends AbstractTransporterTest  {
    private TransporterPort tp = null;

	@Override
	protected void populate() {

	}
	
    @Test //Testing attributes
    public void success() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv1 = port1.requestJob("Lisboa", "Faro", 40);
    	JobView jv2 = port2.requestJob("Lisboa", "Braga", 40);
    	
        assertEquals("Job with wrong company name", jv1.getCompanyName(), "UpaTransporter1");
        assertEquals("Job with wrong origin", jv1.getJobOrigin(), "Lisboa");
        assertEquals("Job with wrong destination", jv1.getJobDestination(), "Faro");
        assertEquals("Job with wrong state", jv1.getJobState(), JobStateView.PROPOSED);
        
        assertEquals("Job with wrong company name", jv2.getCompanyName(), "UpaTransporter2");
        assertEquals("Job with wrong origin", jv2.getJobOrigin(), "Lisboa");
        assertEquals("Job with wrong destination", jv2.getJobDestination(), "Braga");
        assertEquals("Job with wrong state", jv2.getJobState(), JobStateView.PROPOSED);
        
    }
  
    @Test //Testing price = 10
    public void priceEquals10() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 10;
    	JobView jv = port1.requestJob("Lisboa", "Faro", price);
    	
        assertTrue("Price should be less than 10", jv.getJobPrice()<price);
    }

    @Test //Testing lower than client price
    public void priceLowerThanClientPrice() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 40;
    	JobView jv = port1.requestJob("Lisboa", "Faro", price);
    	
        assertTrue("Price should be lower than client price",jv.getJobPrice()<price);
    }
    
    @Test //Testing higher than client price
    public void priceHigherThanClientPrice() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 41;
    	JobView jv = tp.requestJob("Lisboa", "Braga", price);
		
    	assertTrue("Price should be higher than client price",jv.getJobPrice()>price); 
    } 
    
    @Test //Testing even price lower than 10
    public void priceEvenLowerThan10() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 2;
    	JobView jv = tp.requestJob("Lisboa", "Braga", price);
    	
    	assertTrue("Price should be lower than client price", jv.getJobPrice() < price);     
        
    }
    
    @Test //Testing odd price lower than 10
    public void priceOddLowerThan10() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 1;
    	JobView jv = tp.requestJob("Lisboa", "Braga", price);
	
    	assertTrue("Price should be lower than client price", jv.getJobPrice()<price);
    }
    
    @Test //Testing price = 0
    public void priceEquals0() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 0;
    	JobView jv = tp.requestJob("Lisboa", "Braga", price);
	
    	assertNull("Shouldn't have a job with a 0 price offer", jv);
    }
    
    @Test (expected = BadPriceFault_Exception.class) //Testing negative price
    public void negativePrice() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	 port.requestJob("Lisboa", "Braga", -1);
    }
    
    @Test //Testing price 100
    public void priceEquals100() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 100;
    	JobView jv = tp.requestJob("Lisboa", "Braga", price);
			
    	assertTrue("Price should be lower than client price", jv.getJobPrice()<price);
    }
    
    @Test //Testing more than max price
    public void priceHigherThan100() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = tp.requestJob("Lisboa", "Braga", 101);
    	
    	assertNull("Shouldn't have a job with a higher than 100 price offer", jv);
    }
    
    @Test (expected = BadLocationFault_Exception.class) //Testing incorrect destination
    public void incorrectDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {

    	port.requestJob("Lisboa", "Montijo", 40);

    }
    
    @Test (expected = BadLocationFault_Exception.class) //Testing incorrect origin
    public void incorrectOrigin() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	port.requestJob("Alcochete", "Braga", 40);
    
    }
    
    @Test //Testing no operation on destination
    public void noOperationDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = port.requestJob("Lisboa", "Beja", 40);
        assertNull("Transporter doesn't operate on Destination",jv); 
    }
    
    @Test //Testing no operation on origin
    public void noOperationOrigin() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = port.requestJob("Leiria", "Braga", 40);
        assertNull("Transporter doesn't operate on Destination", jv); 
    }
    
    @Test //Testing origin-destination exchange
    public void originDestinationExchange() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = port.requestJob("Braga", "Lisboa", 40);
        assertNull("There are no jobs for this route", jv); 
    }
    
    @Test (expected = BadLocationFault_Exception.class) //Testing empty origin-destination
    public void emptyOriginDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = port1.requestJob("", "", 40);
        assertNull("There are no jobs for this route", jv); 
    }

}