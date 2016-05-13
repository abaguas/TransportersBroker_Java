package pt.upa.transporter.ws.cli;

import java.io.ByteArrayInputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.stream.StreamSource;

import org.junit.AfterClass;
import org.junit.BeforeClass;


/**
 *  Abstract handler test suite
 */
public abstract class AbstractHandlerTest {

    // static members

    /** hello-ws SOAP request message captured with LoggingHandler */
    protected static final String HELLO_SOAP_REQUEST = 
    "<S:Envelope "
    + "xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\""
    + "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
    + "<SOAP-ENV:Header/>"
    + "<S:Body>"
    + "<ns2:decideJob xmlns:ns2=\"http://ws.transporter.upa.pt/\">"
    + "<id>100001</id>"
    + "<accept>true</accept>"
    + "</ns2:decideJob>"
    + "</S:Body>"
    + "</S:Envelope>";



    /** hello-ws SOAP response message captured with LoggingHandler */
    protected static final String HELLO_SOAP_RESPONSE =
    	"<S:Envelope "
    	+ "xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\""
    	+ " xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
    	+ "<SOAP-ENV:Header>"
    	+ "<t:transporter xmlns:t=\"http://transporter\">UpaTransporter1</t:transporter>"
    	+ "<n:nonce xmlns:n=\"http://nonce\">k2V1YUz5zVze4JCIY5anSA==</n:nonce>"
    	+ "<d:digest xmlns:d=\"http://digest\">TiFKI/l11yl863GbW+KrrmkZ2W0nr5muBIqAFMvnk5onDmx"
    	+ "A6zD0yhiJXLzviDgaBVyoWCHSeLqfesszqk4Y++IalVkbIykl84JB7EIKpz61BJOyqjqjKVIgSeoxjeGu5P"
    	+ "WV9N3dus9nCJFHSr4yFlRJ2xVpgRIIIMwQIe3Mi5sHG+Q7yhSpkBK0yLA1RcD+BIgaqrp4nyCQVRh5Gm9VL"
    	+ "+G7swLd8JtQ8jN2xFuOdT4dFLs3BJnCG3ju+QpNbfOVhYn855ZKOcpf9WicBk4/LM2ssUQKAJwLrAPpUGe8"
    	+ "ukjsA2Sb1qvE3BfGKgU6h9sWHFhrIver7FDV2T2pq8elDg==</d:digest>"
    	+ "</SOAP-ENV:Header>"
    	+ "<S:Body>"
    	+ "<ns2:decideJobResponse xmlns:ns2=\"http://ws.transporter.upa.pt/\">"
    	+ "<return>"
    	+ "<companyName>UpaTransporter1</companyName>"
    	+ "<jobIdentifier>100001</jobIdentifier>"
    	+ "<jobOrigin>Lisboa</jobOrigin>"
    	+ "<jobDestination>Beja</jobDestination>"
    	+ "<jobPrice>165</jobPrice>"
    	+ "<jobState>ACCEPTED</jobState>"
    	+ "</return>"
    	+ "</ns2:decideJobResponse>"
    	+ "</S:Body>"
    	+ "</S:Envelope>";



    /** SOAP message factory */
    protected static final MessageFactory MESSAGE_FACTORY;

    static {
        try {
            MESSAGE_FACTORY = MessageFactory.newInstance();
        } catch(SOAPException e) {
            throw new RuntimeException(e);
        }
    }


    // helper functions

    protected static SOAPMessage byteArrayToSOAPMessage(byte[] msg) throws Exception {
        ByteArrayInputStream byteInStream = new ByteArrayInputStream(msg);
        StreamSource source = new StreamSource(byteInStream);
        SOAPMessage newMsg = newMsg = MESSAGE_FACTORY.createMessage();
        SOAPPart soapPart = newMsg.getSOAPPart();
        soapPart.setContent(source);
        return newMsg;
    }


    // one-time initialization and clean-up

    @BeforeClass
    public static void oneTimeSetUp() {

    }

    @AfterClass
    public static void oneTimeTearDown() {

    }

}
