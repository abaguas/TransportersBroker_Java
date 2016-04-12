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


	@Override
	protected void populate() {
		port = new TransporterPort("UpaTransporter2");
		Job j1 = new Job("1", "Lisboa", "Braga");
		Job j2 = new Job("2", "Lisboa", "Porto");

		port.getJobs().add(j1);
		port.getJobs().add(j2);

	}
	
    @Test //Testing companyName
    public void success() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = port.requestJob("Lisboa", "Braga", 40);
    	
        assertEquals(jv.getCompanyName(), "UpaTransporter2");
        assertEquals(jv.getJobOrigin(), "Lisboa");
        assertEquals(jv.getJobDestination(), "Braga");
        assertEquals(jv.getJobIdentifier(), "1");
        assertEquals(jv.getJobState(), "PROPOSED"); 
    }
    
    @Test //Testing price = 10
    public void test5() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 10;
    	JobView jv = port.requestJob("Lisboa", "Braga", price);
    	
        assertTrue(jv.getJobPrice()<price);
    }

    @Test //Testing less than price
    public void test6() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 40;
    	JobView jv = port.requestJob("Lisboa", "Braga", price);
    	
        assertTrue(jv.getJobPrice()<price);
    }
    
    @Test //Testing more than price
    public void test7() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 41;
    	JobView jv = port.requestJob("Lisboa", "Braga", price);
		
    	assertTrue(jv.getJobPrice()>price); 
    } 
    
    @Test //Testing less even price less than 10
    public void test8() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 2;
    	JobView jv = port.requestJob("Lisboa", "Braga", price);
    	
    	assertTrue(jv.getJobPrice()<price);     
        
    }
    
    @Test //Testing less odd price less than 10
    public void test9() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 1;
    	JobView jv = port.requestJob("Lisboa", "Braga", price);
	
    	assertTrue(jv.getJobPrice()<price);
    }
    
    @Test //Testing price = 0
    public void test4() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 0;
    	JobView jv = port.requestJob("Lisboa", "Braga", price);
	
    	assertNull(jv);
    }
    
    @Test (expected = BadPriceFault_Exception.class) //Testing negative price
    public void test10() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	 port.requestJob("Lisboa", "Braga", -1);
    }
    
    @Test //Testing price 100
    public void test11() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	int price = 100;
    	JobView jv = port.requestJob("Lisboa", "Braga", price);
			
    	assertTrue(jv.getJobPrice()<price);
    }
    
    @Test //Testing more than max price
    public void test12() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = port.requestJob("Lisboa", "Braga", 101);
    	
    	assertNull(jv);
    }
    
    @Test (expected = BadLocationFault_Exception.class) //Testing incorrect destination
    public void test14() throws BadLocationFault_Exception, BadPriceFault_Exception {

    	port.requestJob("Lisboa", "Montijo", 40);

    }
    
    @Test (expected = BadLocationFault_Exception.class) //Testing incorrect origin
    public void test15() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	port.requestJob("Alcochete", "Braga", 40);
    
    }
    
    @Test //Testing no operation on destination
    public void test16() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = port.requestJob("Lisboa", "Beja", 40);
        assertNull(jv); 
    }
    
    @Test //Testing no operation on origin
    public void test17() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = port.requestJob("Leiria", "Braga", 40);
        assertNull(jv); 
    }
    
    @Test //Testing origin-destination exchange
    public void test18() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	JobView jv = port.requestJob("Braga", "Lisboa", 40);
        assertNull(jv); 
    }
    

}