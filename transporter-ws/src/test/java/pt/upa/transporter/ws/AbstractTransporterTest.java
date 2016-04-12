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
	protected static TransporterPort port;

    // one-time initialization and clean-up

	protected abstract void populate();
	
    @BeforeClass
    public static void oneTimeSetUp() {
    	port = new TransporterPort("UpaTransporter1");
    }

    @AfterClass
    public static void oneTimeTearDown() {
    	port = null;
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