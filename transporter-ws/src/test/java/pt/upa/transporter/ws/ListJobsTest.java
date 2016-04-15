package pt.upa.transporter.ws;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 *  Unit ListJobsTest
 *  
 *  Invoked by Maven in the "test" life-cycle phase
 *  If necessary, should invoke "mock" remote servers 
 */

public class ListJobsTest extends AbstractTransporterTest {
	
	protected void populate() {
	}
	
     // tests
    @Test
    public void successTest() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	JobView jv1 = port1.requestJob("Lisboa", "Setúbal", 15);
    	JobView jv2 = port1.requestJob("Lisboa",  "Faro", 75);
    	
    	
    	ArrayList<JobView> jobViews = (ArrayList<JobView>) port1.listJobs();
    		
    	assertNotNull("jobViews was not created", jobViews);
    	assertEquals("incorrect number of jobs", 2, jobViews.size());

    	assertEquals("first job not correct", jv1, jobViews.get(0));
    	assertEquals("second job not correct", jv2, jobViews.get(1));
    	
    	assertEquals("incorrect company name", "UpaTransporter1", jobViews.get(0).getCompanyName());    	
    	assertEquals("incorrect origin", "Lisboa", jv1.getJobOrigin());
    	assertEquals("incorrect destination", "Setúbal", jv1.getJobDestination());
    	assertEquals("incorrect state", "PROPOSED", jv1.getJobState());
    	
    	assertEquals("incorrect company name", "UpaTransporter1", jobViews.get(1).getCompanyName());
    	assertEquals("incorrect origin", "Lisboa", jv2.getJobOrigin());
    	assertEquals("incorrect destination", "Faro", jv2.getJobDestination());
    	assertEquals("incorrect state", "PROPOSED", jv2.getJobState());

    }  

}


