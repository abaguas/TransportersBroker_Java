package pt.upa.broker.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public class PingTest extends AbstractBrokerTest {

	@Override
	protected void populate() {
	
	}
	
    @Test
    public void success() {
    	String ping = port.ping("UpaTransporter1");
    	assertNotNull("Ping is null", ping);
    	assertEquals("Unsuccessful ping", ping, "OK");
    }
}
