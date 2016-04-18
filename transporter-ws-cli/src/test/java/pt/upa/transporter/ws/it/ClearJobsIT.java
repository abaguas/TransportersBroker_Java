package pt.upa.transporter.ws.it;

import org.junit.*;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class ClearJobsIT extends AbstractIT {

	@Override
	protected void populate() {		
	}
	
	 // tests
    @Test
    public void success() {
    	tEven.clearJobs();
    	tOdd.clearJobs();
    }


}
