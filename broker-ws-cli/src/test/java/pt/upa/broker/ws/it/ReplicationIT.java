//TESTS for demonstration
/*package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;

import pt.upa.broker.ws.TransportView;

public class ReplicationIT extends AbstractIT {

	@Test
	public void transportRequestsReflectsOnBothBrokers() throws Exception {
		CLIENT.clearTransports();// To start fresh
		int requestedPriceSouth = 39;
		String idSouth = CLIENT.requestTransport(SOUTH_1, SOUTH_2, requestedPriceSouth);
		int requestedPriceNorth = 88;
		String idNorth = CLIENT.requestTransport(NORTH_1, NORTH_2, requestedPriceNorth);
		
		Collection<TransportView> transportViews = CLIENT.listTransports();
		
		assertTrue(transportViews.size() == 2);
		for (TransportView transportView: transportViews) {
			assertTrue(transportView.getId().equals(idSouth) || transportView.getId().equals(idNorth));
			if (transportView.getId().equals(idSouth)) {
				assertTrue(transportView.getDestination().equals(SOUTH_2) );
				assertTrue(transportView.getOrigin().equals(SOUTH_1) );
				assertTrue(transportView.getPrice() < 38);
				assertTrue(transportView.getTransporterCompany().equals("UpaTransporter1"));	
			}
			else {
				assertTrue(transportView.getDestination().equals(NORTH_2) );
				assertTrue(transportView.getOrigin().equals(NORTH_1) );
				assertTrue(transportView.getPrice() < 88);
				assertTrue(transportView.getTransporterCompany().equals("UpaTransporter4"));	
			}
		}
		
		System.out.println(processToKill);
		Runtime.getRuntime().exec("kill -9 "+ processToKill);

		transportViews = CLIENT.listTransports();
		
		assertTrue(transportViews.size() == 2);
		for (TransportView transportView: transportViews) {
			assertTrue(transportView.getId().equals(idSouth) || transportView.getId().equals(idNorth));
			if (transportView.getId().equals(idSouth)) {
				assertTrue(transportView.getDestination().equals(SOUTH_2) );
				assertTrue(transportView.getOrigin().equals(SOUTH_1) );
				assertTrue(transportView.getPrice() < 38);
				assertTrue(transportView.getTransporterCompany().equals("UpaTransporter1"));	
			}
			else {
				assertTrue(transportView.getDestination().equals(NORTH_2) );
				assertTrue(transportView.getOrigin().equals(NORTH_1) );
				assertTrue(transportView.getPrice() < 88);
				assertTrue(transportView.getTransporterCompany().equals("UpaTransporter4"));	
			}
		}
	}
	
	
	@Test
	public void clearTransportsReflectsOnBothBrokers() throws Exception {
		CLIENT.clearTransports();// To start fresh
		int requestedPriceSouth = 39;
		CLIENT.requestTransport(SOUTH_1, CENTER_1, requestedPriceSouth);
		int requestedPriceNorth = 38;
		CLIENT.requestTransport(NORTH_1, NORTH_2, requestedPriceNorth);
		
		assertTrue(CLIENT.listTransports().size() == 2);
		
		CLIENT.clearTransports();
		assertTrue(CLIENT.listTransports().isEmpty());

		Runtime.getRuntime().exec("kill -9 "+ processToKill);

		ArrayList<TransportView> tv = (ArrayList<TransportView>) CLIENT.listTransports();
		
		assertTrue(tv.isEmpty());
	}
	
	
	@Test
	public void transportViewIsTheSameAtBothBrokers() throws Exception {
		int requestedPriceSouth = 39;
		String id = CLIENT.requestTransport(SOUTH_1, SOUTH_2, requestedPriceSouth);		
		TransportView tv = CLIENT.viewTransport(id);
		
		Runtime.getRuntime().exec("kill -9 "+ processToKill);

		TransportView tv2 = CLIENT.viewTransport(id);
		assertEquals(tv.getDestination(), tv2.getDestination());
		assertEquals(tv.getOrigin(), tv2.getOrigin());
		assertEquals(tv.getId(), tv2.getId());
		assertEquals(tv.getPrice(), tv2.getPrice());
		assertEquals(tv.getTransporterCompany(), tv2.getTransporterCompany());
	}
}*/
