//package pt.upa.broker.ws.it;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//
//import java.util.ArrayList;
//
//import org.junit.Test;
//
//import pt.upa.broker.ws.InvalidPriceFault_Exception;
//import pt.upa.broker.ws.TransportView;
//import pt.upa.broker.ws.UnavailableTransportFault_Exception;
//import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
//import pt.upa.broker.ws.UnknownLocationFault_Exception;
//
//public class clearTransportsIT extends AbstractIT {
//	
//	@Test
//    public void successTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception  {
//		client.requestTransport("Faro", "Lisboa", 45);
//		client.clearTransports();
//		ArrayList<TransportView> tv = (ArrayList<TransportView>) client.listTransports();
//		
//		assertNotNull("should exist a array of views", tv);
//		assertEquals("Size should be 3", tv.size(), 0);
//	}
//
//}
