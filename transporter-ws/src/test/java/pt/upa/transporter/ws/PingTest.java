package pt.upa.transporter.ws;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *  Unit Ping Test
 *  
 *  Invoked by Maven in the "test" life-cycle phase
 *  If necessary, should invoke "mock" remote servers 
 */

public class PingTest extends AbstractTransporterTest {

	@Override
	protected void populate() {
		port = new TransporterPort("UpaTransporter1");
	
	}
	
    @Test
    public void test() {
    	String ping = port.ping("HelloWorld");
    	assertNotNull("Ping is null", ping);
    	assertEquals("Unsuccessful ping", ping, "UpaTransporter1");
    }

	

}