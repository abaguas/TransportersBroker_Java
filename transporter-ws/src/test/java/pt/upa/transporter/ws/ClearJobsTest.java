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
	}
	
    // tests
    @Test
    public void success() {
    	port1.clearJobs();
    	port2.clearJobs();
    	
    	ArrayList<Job> jobs1 = port1.getJobs();
    	ArrayList<Job> jobs2 = port2.getJobs();

    	assertNotNull("structure deleted", jobs1);
    	assertEquals("jobs not deleted", 0, jobs1.size());
    	assertNotNull("structure deleted", jobs2);
    	assertEquals("jobs not deleted", 0, jobs2.size());
    }



}