package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ApplicationIT extends AbstractIT {
	

	@Test
    public void oddPingTest() {
    	String ping = client.ping("HelloWorld");
    	assertNotNull("Ping is null", ping);
    	assertEquals("Unsuccessful ping", ping, "UpaTransporter1");
    }

}
