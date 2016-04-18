package pt.upa.broker.ws;

import static org.junit.Assert.*;

import java.util.Collection;

import javax.xml.ws.WebServiceException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;

public class BrokerMockTest extends AbstractBrokerTest{
	// static members

/*
    private static JobView jv1;

    
    @BeforeClass
    public static void oneTimeSetUp() {
    	jv1 = new JobView();
    	jv1.setCompanyName("UpaTransporter1");
    	jv1.setJobDestination("Faro");
    	jv1.setJobIdentifier("0000000001");
    	jv1.setJobOrigin("Lisboa");
    	jv1.setJobPrice(0);
    	jv1.setJobState(JobStateView.PROPOSED);
    	
    	
    	
    }

    @AfterClass
    public static void oneTimeTearDown() {
    }



    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    
    @Test (expected=WebServiceException.class)
    public void testLookup(
        @Mocked final  UDDINaming uddi)
        throws Exception {

        new Expectations() {{
        	new UDDINaming("http://localhost:9090");
            uddi.lookup("UpaTransporter1");
            result = "http://localhost:8081/transporter-ws/endpoint";
           
            result = new WebServiceException("Fail on lookup");
        }};

        BrokerPort bp = new BrokerPort("http://localhost:9090"); 
       
        try {
            bp.lookUp("UpaTransporter1");
        } catch(WebServiceException e) {
            // exception is not expected
            fail();
        }

        // second call to mocked server
        try {
            bp.ping("UpaTransporter1");
            fail();
        } catch(WebServiceException e) {
            // exception is expected
            assertEquals("Fail on lookup", e.getMessage());
        }
    }

    @Test (expected=WebServiceException.class)
    public void testPing(
        @Mocked final  TransporterClient tc1,
        @Mocked final  UDDINaming uddi)
        throws Exception {

        
        new Expectations() {{
        	new UDDINaming("http://localhost:9090");
            uddi.lookup("UpaTransporter1");
            result = "http://localhost:8081/transporter-ws/endpoint";
            new TransporterClient(result.toString());
            
            
            tc1.ping("UpaTransporter1");
            
            result = "UpaTransporter1";
          
            result = new WebServiceException("Unreachable");

        }};

        BrokerPort bp = new BrokerPort("http://localhost:9090"); 

        try {
            bp.ping("UpaTransporter1");
        } catch(WebServiceException e) {
            // exception is not expected
            fail();
        }

        // second call to mocked server
        try {
            bp.ping("UpaTransporter1");
            fail();
        } catch(WebServiceException e) {
            // exception is expected
            assertEquals("Unreachable", e.getMessage());
        }
    }
    
    @Test (expected=WebServiceException.class)
    public void testRequestTransport(
        @Mocked final  TransporterClient tc1,
        @Mocked final  UDDINaming uddi)
        throws Exception {

        
        new Expectations() {{
        	new UDDINaming("http://localhost:9090");
            uddi.lookup("UpaTransporter1");
            result = "http://localhost:8081/transporter-ws/endpoint";
            new TransporterClient(result.toString());
            
            
            tc1.requestJob("Lisboa", "Faro", 11);
            
            result=jv1;
          
            result = new WebServiceException("Error in requestJob");

        }};

        BrokerPort bp = new BrokerPort("http://localhost:9090"); 

        try {
            bp.requestTransport("Lisboa","Faro",1);
        } catch(WebServiceException e) {
            // exception is not expected
            fail();
        }

        // second call to mocked server
        try {
        	bp.requestTransport("Lisboa","Faro",1);
            fail();
        } catch(WebServiceException e) {
            // exception is expected
            assertEquals("Error in requestJob", e.getMessage());
        }
    }*/
    


	@Override
	protected void populate() {
		
		
	}

}
