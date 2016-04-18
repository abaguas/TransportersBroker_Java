package pt.upa.broker.ws.it;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportStateView;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;


public class ViewTransportIT extends AbstractIT {
	

	@Test
    public void validIDTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		String id = client.requestTransport("Faro", "Lisboa", 45);
		System.out.println(id);
		TransportView tv = client.viewTransport(id);
		
		assertNotNull("should exist a view", tv);
		assertTrue("state should be booked", tv.getState().equals(TransportStateView.BOOKED));
    }
	
	@Test
    public void price10Test() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		String id = client.requestTransport("Faro", "Lisboa", 10);
		System.out.println(id);
		TransportView tv = client.viewTransport(id);
		
		assertNotNull("should exist a view", tv);
		assertTrue("Price should be lower than 10", tv.getPrice()<10);
    }
	
	@Test
    public void priceBellow10Test() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		String id = client.requestTransport("Faro", "Lisboa", 5);
		System.out.println(id);
		TransportView tv = client.viewTransport(id);
		
		assertNotNull("should exist a view", tv);
		assertTrue("Price should be lower than 5", tv.getPrice()<5);
    }
	
	@Test
    public void price1Test() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception, UnknownTransportFault_Exception {
		String id = client.requestTransport("Faro", "Lisboa", 1);
		System.out.println(id);
		TransportView tv = client.viewTransport(id);
		
		assertNotNull("should exist a view", tv);
		assertTrue("Price should be lower than 1", tv.getPrice()<1);
		System.out.println("--------------------------------------------------------------");
		System.out.println(tv.getPrice());
		System.out.println("--------------------------------------------------------------");
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