package pt.upa.broker.ws;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobView;

public class RequestTransportTest extends AbstractBrokerTest {

	@Override
	protected void populate() {
		port = new BrokerPort(""); //FIXME
	}

	@Test
    public void succcessTest() throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
	UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {

		String s = port.requestTransport("Lisboa", "Braga", 40);

	    assertEquals("Job with wrong state", s, "PROPOSED");
    }
	
	

}
