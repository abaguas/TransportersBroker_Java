package pt.upa.transporter.ws.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.junit.Test;

import mockit.Mocked;
import mockit.StrictExpectations;
import pt.upa.transporter.ws.handler.TransporterClientHandler;


/**
 *  Handler test suite
 */
public class TransporterClientHandlerTest extends AbstractHandlerTest {

    // tests
	private String TransporterName;


    @Test
    public void testHeaderHandlerInbound(
        @Mocked final SOAPMessageContext soapMessageContext)
        throws Exception {

        // Preparation code not specific to JMockit, if any.
        final String soapText = HELLO_SOAP_RESPONSE;
        //System.out.println(soapText);

        final SOAPMessage soapMessage = byteArrayToSOAPMessage(soapText.getBytes());
        final Boolean soapOutbound = false;

        // an "expectation block"
        // One or more invocations to mocked types, causing expectations to be recorded.
        new StrictExpectations() {{
            soapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            result = soapOutbound;


            soapMessageContext.get("Transporter");
            result="UpaTransporter1";
            
            soapMessageContext.getMessage();
            result = soapMessage;

        }};

        // Unit under test is exercised.
        TransporterClientHandler handler = new TransporterClientHandler();
        boolean handleResult = handler.handleMessage(soapMessageContext);


        // Additional verification code, if any, either here or before the verification block.

        // assert that message would proceed normally
        assertTrue(handleResult);

        //soapMessage.writeTo(System.out);
    }
    
    @Test
    public void testHeaderHandlerOutbound(
        @Mocked final SOAPMessageContext soapMessageContext)
        throws Exception {

        // Preparation code not specific to JMockit, if any.
        final String soapText = HELLO_SOAP_RESPONSE;
        // System.out.println(soapText);

        final SOAPMessage soapMessage = byteArrayToSOAPMessage(soapText.getBytes());
        final Boolean soapOutbound = true;
        // an "expectation block"
        // One or more invocations to mocked types, causing expectations to be recorded.
        new StrictExpectations() {{
            soapMessageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            result = soapOutbound;

            soapMessageContext.get("Transporter");
            result="UpaTransporter1";
            
            soapMessageContext.getMessage();
            result = soapMessage;


            
        }};

        // Unit under test is exercised.
        TransporterClientHandler handler = new TransporterClientHandler();
        boolean handleResult = handler.handleMessage(soapMessageContext);

        // Additional verification code, if any, either here or before the verification block.

        // assert that message would proceed normally
        assertTrue(handleResult);

        // assert header
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        SOAPHeader soapHeader = soapEnvelope.getHeader();
        assertNotNull(soapHeader);

        // assert header element
        Name name = soapEnvelope.createName("transporter", "t", "http://transporter");
        Iterator it = soapHeader.getChildElements(name);
        assertTrue(it.hasNext());

        // assert header element value
        SOAPElement element = (SOAPElement) it.next();
        String valueString = element.getValue();
        assertEquals("UpaTransporter1", valueString);

        Name name2 = soapEnvelope.createName("digest", "d", "http://digest");
        Iterator it2 = soapHeader.getChildElements(name2);
        assertTrue(it2.hasNext());

        // assert header element value
        SOAPElement element2 = (SOAPElement) it2.next();
        String valueString2 = element2.getValue();
        assertNotNull(valueString2);
        
        Name name3 = soapEnvelope.createName("nonce", "n", "http://nonce");
        Iterator it3 = soapHeader.getChildElements(name3);
        assertTrue(it3.hasNext());

        // assert header element value
        SOAPElement element3 = (SOAPElement) it3.next();
        String valueString3 = element3.getValue();
        assertNotNull(valueString3);
    }

}
