package pt.upa.broker.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;


public class ViewTransportIT extends AbstractIT {
	

	@Test
    public void validIDTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		String id = client.requestTransport("Faro", "Lisboa", 45);
		TransportView tv = client.viewTransport(id);
		
		assertNotNull("should exist a view", tv);
    }
	
	@Test(expected = UnknownTransportFault_Exception.class)
    public void invalidIDTest() throws UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		client.viewTransport("aaa");
    }
	
	@Test(expected = UnknownTransportFault_Exception.class)
    public void nullIDTest() throws UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		client.viewTransport(null);
    }

}