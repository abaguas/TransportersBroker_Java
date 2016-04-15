package pt.upa.transporter.ws.it;

import org.junit.*;
import static org.junit.Assert.*;

public class PingIT extends AbstractIT {

	@Override
	protected void populate() {
		// TODO Auto-generated method stub
		
	}
	
	@Test
    public void oddPingTest() {
    	String ping = tOdd.ping("HelloWorld");
    	assertNotNull("Ping is null", ping);
    	assertEquals("Unsuccessful ping", ping, "UpaTransporter1");
    }
	@Test
	public void EvenPingTest() {
    	String ping = tEven.ping("HelloWorld");
    	assertNotNull("Ping is null", ping);
    	assertEquals("Unsuccessful ping", ping, "UpaTransporter4");
    }

}
