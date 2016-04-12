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

	private TransporterPort t1;
	
	@Override
	protected void populate() {
		t1 = new TransporterPort("UpaTransporterPort1");
		Job j1 = new Job("20", "Lisboa", "Setúbal"); //id=20
		Job j2 = new Job("25", "Lisboa", "Faro");
		j1.setCompanyName("UpaTransporter1");
		j2.setCompanyName("UpaTransporter1");
		j1.setPrice(15);
		j2.setPrice(75);
		t1.addJob(j1);
		t1.addJob(j2);
		t1.addRequestedJob(j2); // so j2 é que foi requested
	}
	
    // tests
    @Test
    public void success() {
    	t1.clearJobs();
    	ArrayList<Job> jobs = t1.getJobs();
    	ArrayList<Job> requestedJobs = t1.getRequestedJobs();

    	assertNotNull("structure deleted", jobs);
    	assertNotNull("structure deleted", requestedJobs);
    	assertEquals("jobs not deleted", 0, jobs.size());
    	assertEquals("requestedJobs not deleted", 0, requestedJobs.size());

    }



}