package pt.upa.transporter.ws;

import org.junit.*;

/**
 *  Unit AbstractTransporterTest
 *  
 *  Invoked by Maven in the "test" life-cycle phase
 *  If necessary, should invoke "mock" remote servers 
 */
public abstract class AbstractTransporterTest {

    // static members
	protected static TransporterPort port1;
	protected static TransporterPort port2;

    // one-time initialization and clean-up

	protected abstract void populate();
	
    @BeforeClass
    public static void oneTimeSetUp() {
    	port1 = new TransporterPort("UpaTransporter1");
    	port2 = new TransporterPort("UpaTransporter2");
    }

    @AfterClass
    public static void oneTimeTearDown() {
    	port1 = null;
    	port2 = null;
    }


    // members


    // initialization and clean-up for each test

    @Before
    public void setUp() {
    	populate();	
    }

    @After
    public void tearDown() {
    	port1.clearJobs();
    	port2.clearJobs();
    }


    // tests

    @Test
    public void test() {

        // assertEquals(expected, actual);
        // if the assert fails, the test fails
    }

}