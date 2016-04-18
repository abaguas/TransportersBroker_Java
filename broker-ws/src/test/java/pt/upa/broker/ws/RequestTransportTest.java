package pt.upa.broker.ws;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;

public class RequestTransportTest extends AbstractBrokerTest {

	@Override
	protected void populate() {
	}
	
	@Test (expected = UnknownLocationFault_Exception.class)
    public void wrongOrigin() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
	UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {

		port.requestTransport("Montijo", "Faro", 40);
		
    }
	
	@Test (expected = UnknownLocationFault_Exception.class)
    public void wrongDestination() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
	UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {

		port.requestTransport("Lisboa", "Montijo", 40);

    }
	
	@Test (expected = InvalidPriceFault_Exception.class)
    public void negativePrice() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
	UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {

		port.requestTransport("Lisboa", "Faro", -1);

    }
	
	//only foe odd
	@Test (expected = UnavailableTransportPriceFault_Exception.class)
    public void noRouteOnOperation() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
	UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {

		port.requestTransport("Santarém", "Beja", 40);

    }
	
	@Test (expected = UnavailableTransportFault_Exception.class)
    public void originDestinationOnCenter() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
	UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {

		port.requestTransport("Viseu", "Leiria", 40);

    }
	
	@Test(expected = UnavailableTransportFault_Exception.class)
    public void priceTooHigh() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
	UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {

		port.requestTransport("Lisboa", "Faro", 101);
    }
	
	

}
