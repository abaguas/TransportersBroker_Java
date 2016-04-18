package pt.upa.broker.ws.it;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;


public class RequestJobIT extends AbstractIT {
	

	@Test
    public void successTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		String id = client.requestTransport("Faro", "Lisboa", 45);
		System.out.println(id);
		assertNotNull(id);
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
		String id = client.requestTransport("Lisboa", "Faro", 99);
		assertNotNull("should return a id", id);
    }

}