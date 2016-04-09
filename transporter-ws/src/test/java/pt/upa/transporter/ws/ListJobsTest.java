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

	TransporterPort t1;
	TransporterPort t2;
	
	protected void populate() {
		t1 = new TransporterPort("UpaTransporter1");
		t2 = new TransporterPort("UpaTransporter2");
		Job j1 = new Job("1", "Lisboa", "Setúbal");
		Job j2 = new Job("1", "Lisboa", "Faro");
		j1.setCompanyName("UpaTransporter1");
		j2.setCompanyName("UpaTransporter1");
		j1.setPrice(15);
		j1.setPrice(75);
		t1.addJob(j1);
		t1.addJob(j2);
	}
	
	
	
     // tests
    @Test
    public void succesTest() {
    	//ArrayList<JobView> jobs = t1.listJobs();
        
    	// assertEquals(expected, actual);
        // if the assert fails, the test fails
    }
   

}


