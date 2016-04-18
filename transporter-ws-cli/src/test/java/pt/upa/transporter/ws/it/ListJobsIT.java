package pt.upa.transporter.ws.it;

import org.junit.*;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class ListJobsIT extends AbstractIT {

	@Override
	protected void populate() {		
	}
	
	  // tests
    @Test
    public void successTest() throws BadLocationFault_Exception, BadPriceFault_Exception {
    	
    	JobView jv1 = tOdd.requestJob("Lisboa", "Setúbal", 15);
    	JobView jv2 = tOdd.requestJob("Lisboa",  "Faro", 75);
    	
    	
    	ArrayList<JobView> jobViews = (ArrayList<JobView>) tOdd.listJobs();
    		
    	assertNotNull("jobViews was not created", jobViews);
    	assertEquals("incorrect number of jobs", 2, jobViews.size());

    	//assertEquals("first job not correct", jv1, jobViews.get(1));
    	//assertEquals("second job not correct", jv2, jobViews.get(2));
    	
    	assertEquals("incorrect company name", "UpaTransporter1", jobViews.get(0).getCompanyName());    	
    	assertEquals("incorrect origin", "Lisboa", jv1.getJobOrigin());
    	assertEquals("incorrect destination", "Setúbal", jv1.getJobDestination());
    	assertEquals("incorrect state", JobStateView.PROPOSED, jv1.getJobState());
    	
    	assertEquals("incorrect company name", "UpaTransporter1", jobViews.get(1).getCompanyName());
    	assertEquals("incorrect origin", "Lisboa", jv2.getJobOrigin());
    	assertEquals("incorrect destination", "Faro", jv2.getJobDestination());
    	assertEquals("incorrect state", JobStateView.PROPOSED, jv2.getJobState());

    }  

}
