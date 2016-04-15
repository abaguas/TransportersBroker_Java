package pt.upa.transporter.ws.it;

import org.junit.*;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.exception.ArgumentsMissingException;
import pt.upa.transporter.exception.NoEndpointFoundException;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.transporter.ws.cli.TransporterClientMain;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.registry.JAXRException;

/**
 *  Integration Test example
 *  
 *  Invoked by Maven in the "verify" life-cycle phase
 *  Should invoke "live" remote servers 
 */
public abstract class AbstractIT {

    // static members

	protected static TransporterClient tEven;
	protected static TransporterClient tOdd;

	protected abstract void populate();
	
	
    // one-time initialization and clean-up

    @BeforeClass
    public static void oneTimeSetUp() throws IOException, JAXRException {
		ClientTransporterGetPropertyValues properties = new ClientTransporterGetPropertyValues();
		String propOdd = properties.getPropValues("uddi.url", "ws.name1");
		String propEven = properties.getPropValues("uddi.url", "ws.name2");

		String[] args = propOdd.split(" ");
		String[] args2 = propEven.split(" ");
		System.out.println(args[0]);
		System.out.println(args[1]);
		
        try {
			tOdd = TransporterClientMain.main(args);
	        System.out.println(args2[0]);
	        System.out.println(args2[1]);
	        tEven = TransporterClientMain.main(args2);
        } catch (ArgumentsMissingException ame){
        	ame.getMessage();
        } catch (NoEndpointFoundException nefe){
        	nefe.getMessage();
        }
    }
    
    
    @AfterClass
    public static void oneTimeTearDown() {
    	tOdd = null;
    	tEven = null;
    }


    // members


    // initialization and clean-up for each test

    @Before
    public void setUp() {
    	populate();
    }

    @After
    public void tearDown() {
    }


    // tests

    @Test
    public void test() {

        // assertEquals(expected, actual);
        // if the assert fails, the test fails
    }

}