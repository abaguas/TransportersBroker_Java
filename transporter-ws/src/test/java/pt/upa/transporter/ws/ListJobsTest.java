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

	TransporterPort t1; // t2;
	Job j1, j2, j3, j4;
	
	protected void populate() {
		t1 = new TransporterPort("UpaTransporter1");

		j1 = new Job("Lisboa", "Setúbal");
		j2 = new Job("Lisboa", "Faro");
		j1.setCompanyName("UpaTransporter1");
		j2.setCompanyName("UpaTransporter1");
		j1.setPrice(15);
		j2.setPrice(75);
		t1.addAvailableJob(j1);
		t1.addAvailableJob(j2);
		t1.addRequestedJob(j1);
		t1.addRequestedJob(j2);
	}
	
     // tests
    @Test
    public void successTest() {
  
    	ArrayList<JobView> jv1 = (ArrayList<JobView>) t1.listRequestedJobs();
    		
    	assertNotNull("jobViews was not created", jv1);
    	assertEquals("incorrect number of jobs", 2, jv1.size());
    	//FIXME, UM JOB NAO E UM JOB VIEW //assertEquals("first job not correct", j1, jv1.get(1));
    	//FIXME IGUAL//assertEquals("second job not correct", j2, jv1.get(2));
    	assertEquals("incorrect company name", "UpaTransporter1", j1.getCompanyName());
    	assertEquals("incorrect job identifier", "1", j1.getIdentifier());    	
    	assertEquals("incorrect origin", "Lisboa", j1.getOrigin());
    	assertEquals("incorrect destination", "Setúbal", j1.getDestination());
    	assertEquals("incorrect price", 15, j1.getPrice());
    	assertEquals("incorrect state", "PROPOSED", j1.getState());
    	assertEquals("incorrect company name", "UpaTransporter1", j2.getCompanyName());
    	assertEquals("incorrect job identifier", "2", j2.getIdentifier());    	
    	assertEquals("incorrect origin", "Lisboa", j2.getOrigin());
    	assertEquals("incorrect destination", "Faro", j2.getDestination());
    	assertEquals("incorrect price", 75, j2.getPrice());
    	assertEquals("incorrect state", "PROPOSED", j2.getState());
    }  

}


