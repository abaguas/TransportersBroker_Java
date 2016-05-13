package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.upa.broker.ws.TransportView;

public class TimeoutIT extends AbstractIT {


	@Test
	public void timoutExpiresButPrimaryBrokerDoesntCollapse() throws Exception {
		int requestedPrice = 39;
		String id = CLIENT.requestTransport("Faro", "Lisboa", requestedPrice);
		TransportView tv = CLIENT.viewTransport(id);
		final int price = tv.getPrice().intValue();
		assertTrue(price >= ZERO_PRICE && price <= requestedPrice);
	}
	
	@Test
	public void timoutExpiresAfterPrimaryBrokerCollapses() throws Exception {
		int requestedPrice = 39;
		String id = CLIENT.requestTransport("Faro", "Lisboa", requestedPrice);
		
		Runtime.getRuntime().exec("kill -9 "+ processToKill);
		
		TransportView tv = CLIENT.viewTransport(id);
		final int price = tv.getPrice().intValue();
		assertTrue(price >= ZERO_PRICE && price <= requestedPrice);
	}
	
}