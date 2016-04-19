package pt.upa.broker.ws.it;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class OddAndEvenTranportersIT extends AbstractIT {
	
	@Test
    public void successOddCenterWithLowPriceTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		String id = client.requestTransport("Leiria", "Lisboa", 45);
		assertNotNull(id);
		TransportView tv = client.viewTransport(id);
		assertEquals("Tranporter 1 should have had the best offer", tv.getTransporterCompany(), "UpaTransporter1");
		assertTrue("price should be lower than 45", tv.getPrice()<45);
	}
	
	@Test
    public void successEvenCenterWithLowPriceTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		String id = client.requestTransport("Leiria", "Lisboa", 44);
		assertNotNull(id);
		TransportView tv = client.viewTransport(id);
		assertEquals("Tranporter 4 should have had the best offer", tv.getTransporterCompany(), "UpaTransporter4");
		assertTrue("price should be lower than 44", tv.getPrice()<44);
	}

	@Test(expected = UnavailableTransportPriceFault_Exception.class)
    public void SouthWithEvenPriceTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		client.requestTransport("Leiria", "Portalegre", 46);
	}

	@Test(expected = UnavailableTransportPriceFault_Exception.class)
    public void NorthWithOddPriceTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		client.requestTransport("Leiria", "Viana do Castelo", 43);
	}
	
	@Test
    public void lowerThan1Test() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		String id = client.requestTransport("Faro", "Lisboa", 1);
		assertNotNull(id);
		TransportView tv = client.viewTransport(id);
		assertTrue("price should be 0", tv.getPrice()==0);
    }
	
	@Test
    public void lowerThan10Test() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		String id = client.requestTransport("Faro", "Lisboa", 5);
		assertNotNull(id);
		TransportView tv = client.viewTransport(id);
		assertTrue("price should be 0", tv.getPrice()<5);
    }
	
	@Test(expected = InvalidPriceFault_Exception.class)
    public void badPriceTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		client.requestTransport("Faro", "Lisboa", -2);
    }
	
	@Test(expected = UnknownLocationFault_Exception.class)
    public void badOriginTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		client.requestTransport("Sara", "Lisboa", -2);
    }
	
	@Test(expected = UnknownLocationFault_Exception.class)
    public void badDestinationTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		client.requestTransport("Lisboa", "Sara", -2);
    }
	
	@Test(expected = UnknownLocationFault_Exception.class)
    public void nullDestinationOriginTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		client.requestTransport(null, null, 40);
    }
	
	@Test(expected = UnavailableTransportFault_Exception.class)
    public void TooHighPriceTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		String id = client.requestTransport("Lisboa", "Faro", 101);
    }

	@Test
    public void HighestPossiblePriceTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		String id = client.requestTransport("Lisboa", "Porto", 100);
		assertNotNull("should return a id", id);
    }


	@Test
    public void listTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		client.clearTransports();
		String id = client.requestTransport("Faro", "Lisboa", 45);
		System.out.println(id);
		String id2 = client.requestTransport("Portalegre", "Bragança", 58);
		System.out.println(id2);
		
		ArrayList<TransportView> tv = (ArrayList<TransportView>) client.listTransports();
		assertNotNull("should exist a array of views", tv);
		assertEquals("Size should be 2", tv.size(), 2);
	}
}
