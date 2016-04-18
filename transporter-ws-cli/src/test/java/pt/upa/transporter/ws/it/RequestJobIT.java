package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.*;
import static org.junit.Assert.*;
import mockit.*;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;

public class RequestJobIT extends AbstractIT {

	@Override
	protected void populate() {
	}
	
	@Test //Testing attributes
    public void success() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv1 = tOdd.requestJob("Lisboa", "Faro", 40);
    	JobView jv2 = tEven.requestJob("Lisboa", "Braga", 40);
    	
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
    	JobView jv = tOdd.requestJob("Lisboa", "Faro", price);
        assertTrue("Price should be less than 10", jv.getJobPrice()<price);
    }

    @Test //Testing even price lower than 10
    public void priceEvenLowerThan10() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 2;
    	JobView jv = tEven.requestJob("Lisboa", "Braga", price);
    	
    	assertTrue("Price should be lower than client price", jv.getJobPrice() < price);     
        
    }
    
    @Test //Testing odd price lower than 10
    public void priceOddLowerThan10() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 1;
    	JobView jv = tEven.requestJob("Lisboa", "Braga", price);
	
    	assertTrue("Price should be lower than client price", jv.getJobPrice()<price);
    }
    
    @Test //Testing higher than client price
    public void priceHigherThanClientPrice1() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 40;
    	JobView jv = tOdd.requestJob("Lisboa", "Faro", price);
    	
        assertTrue("Price should be higher than client price",jv.getJobPrice()>price);
    }
    
    @Test //Testing lower than client price
    public void priceLowerThanClientPrice1() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 41;
    	JobView jv = tOdd.requestJob("Lisboa", "Faro", price);
		
    	assertTrue("Price should be lower than client price",jv.getJobPrice()<price); 
    } 
    
    
    @Test //Testing higher than client price
    public void priceHigherThanClientPrice2() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 41;
    	JobView jv = tEven.requestJob("Lisboa", "Braga", price);
    	
        assertTrue("Price should be higher than client price",jv.getJobPrice()>price);
    }
    
    
    @Test //Testing lower than client price
    public void priceLowerThanClientPrice2() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 40;
    	JobView jv = tEven.requestJob("Lisboa", "Braga", price);
		
    	assertTrue("Price should be lower than client price",jv.getJobPrice()<price); 
    }   
    
    
    @Test //Testing price = 0
    public void priceEquals0() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 0;
    	JobView jv = tEven.requestJob("Lisboa", "Braga", price);	
    	assertNull("Shouldn't have a job with a 0 price offer", jv);
    }
 
    
    @Test //Testing price = 100
    public void priceEquals100odd() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 100;
    	JobView jv = tOdd.requestJob("Lisboa", "Faro", price);
			
    	assertTrue("Price should be higher than client price", jv.getJobPrice()>price);
    }

    
    @Test //Testing price = 100
    public void priceEquals100even() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 100;
    	JobView jv = tEven.requestJob("Lisboa", "Braga", price);
			
    	assertTrue("Price should be lower than client price", jv.getJobPrice()<price);
    }
    
    
    @Test //Testing more than max price
    public void priceHigherThan100() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = tEven.requestJob("Lisboa", "Braga", 101);
    	
    	assertNull("Shouldn't have a job with a higher than 100 price offer", jv);
    }
    
    
    @Test //Testing no operation on destination
    public void noOperationDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = tEven.requestJob("Lisboa", "Beja", 40);
        assertNull("Transporter doesn't operate on Destination",jv); 
    }
    
    @Test //Testing no operation on origin
    public void noOperationOrigin() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = tOdd.requestJob("Leiria", "Braga", 40);
        assertNull("Transporter doesn't operate on Destination", jv); 
    }
    

    @Test (expected = BadPriceFault_Exception.class) //Testing negative price
    public void negativePrice() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	 tOdd.requestJob("Lisboa", "Braga", -1);
    }
    
    
    @Test (expected = BadLocationFault_Exception.class) //Testing incorrect origin
    public void incorrectOrigin() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = tEven.requestJob("Alcochete", "Braga", 40);

    }
    
    @Test (expected = BadLocationFault_Exception.class) //Testing incorrect destination
    public void incorrectDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = tEven.requestJob("Lisboa", "Montijo", 40);
    }

    
    @Test (expected = BadLocationFault_Exception.class) //Testing empty origin-destination
    public void emptyOriginDestination() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = tOdd.requestJob("", "", 40);
        assertNull("There are no jobs for this route", jv); 
    }
}
