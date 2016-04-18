package pt.upa.broker.ws.it;

import org.junit.*;

import pt.upa.broker.ws.cli.BrokerClient;
import pt.upa.broker.ws.cli.BrokerClientMain;
import pt.upa.broker.exception.ArgumentsMissingException;
import pt.upa.broker.exception.NoEndpointFoundException;
import pt.upa.broker.ws.it.ClientBrokerGetPropertyValues;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.registry.JAXRException;

/**
 *  Integration Test example
 *  
 *  Invoked by Maven in the "verify" life-cycle phase
 *  Should invoke "live" remote servers 
 */
public class AbstractIT {

	 // static members

		protected static BrokerClient client;		
		
	    // one-time initialization and clean-up

	    @BeforeClass
	    public static void oneTimeSetUp() throws IOException, JAXRException {
			ClientBrokerGetPropertyValues properties = new ClientBrokerGetPropertyValues();
			String arguments = properties.getPropValues();

			String[] args = arguments.split(" ");			

			System.out.println(args[0]);
			System.out.println(args[1]);
			
	        try {
				client = BrokerClientMain.main(args);
	        } catch (ArgumentsMissingException ame){
	        	ame.getMessage();
	        } catch (NoEndpointFoundException nefe){
	        	nefe.getMessage();
	        }
	    }
	    
	    
	@AfterClass
	public static void oneTimeTearDown() {
	 	client = null;
	}
    // members


    // initialization and clean-up for each test

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    	client.clearTransports();
    }


    // tests

    @Test
    public void test() {

        // assertEquals(expected, actual);
        // if the assert fails, the test fails
    }

}