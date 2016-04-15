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
	}
	
    @Test
    public void oddPingTest() {
    	String ping = port1.ping("HelloWorld");
    	assertNotNull("Ping is null", ping);
    	assertEquals("Unsuccessful ping", ping, "UpaTransporter1");
    }
    
    @Test
    public void evenPairPingTest() {
    	String ping = port2.ping("HelloWorld");
    	assertNotNull("Ping is null", ping);
    	assertEquals("Unsuccessful ping", ping, "UpaTransporter2");
    }

}