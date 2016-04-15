package pt.upa.transporter.ws;

import org.junit.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 *  Unit ClearJobsTest
 *  
 *  Invoked by Maven in the "test" life-cycle phase
 *  If necessary, should invoke "mock" remote servers 
 */


public class ClearJobsTest extends AbstractTransporterTest {

	
	@Override
	protected void populate() {
		
//		t1 = new TransporterPort("UpaTransporterPort1");
//		Job j1 = new Job("Lisboa", "Setúbal"); //id=20
//		Job j2 = new Job("Lisboa", "Faro");
//		j1.setCompanyName("UpaTransporter1");
//		j2.setCompanyName("UpaTransporter1");
//		j1.setPrice(15);
//		j2.setPrice(75);
//		t1.addAvailableJob(j1);
//		t1.addAvailableJob(j2);
//		t1.addRequestedJob(j2); // so j2 é que foi requested
		
	}
	
    // tests
    @Test
    public void success() {
    	port1.clearJobs();
    	port2.clearJobs();
    	
    	ArrayList<Job> jobs1 = port1.getAvailableJobs();
    	ArrayList<Job> jobs2 = port2.getAvailableJobs();

    	assertNotNull("structure deleted", jobs1);
    	assertEquals("jobs not deleted", 0, jobs1.size());
    	assertNotNull("structure deleted", jobs2);
    	assertEquals("jobs not deleted", 0, jobs2.size());
    }



}