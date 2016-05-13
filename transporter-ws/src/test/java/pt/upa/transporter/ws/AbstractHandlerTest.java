package pt.upa.transporter.ws;

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
    "<S:Envelope " +
    "xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" "+
    "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"+
    "<SOAP-ENV:Header>" +
    "<t:transporter xmlns:t=\"http://transporter\">UpaTransporter1</t:transporter>"+
    "<n:nonce xmlns:n=\"http://nonce\">CSjE5xCC74ccejaFmxFgXw==</n:nonce>"+
    "<d:digest xmlns:d=\"http://digest\">KmWWgB2Yb+kZCSKSqU/NoVhTNp6oDDFbc1wmzrTmkW0U1rAQR/"
    + "pKy+Wqc/5kqHxxep7ggWR/UVguyMy5cQJMCStQHglVwHaqLVY2XSz5yoLOtnJ8A+xaP6oMihWDK2scTVAxwVe9waESd5gBkfm"
    + "xsHDjEdlDCretT5h+PYHnKfM7PhnyEY4B9gD2DkEWRVgdPqR+ohuXWC4fF8T6hO1eCNvJe37VYIRlgFfsDlpbGuKEJUeaEBRX19"
    + "JW6On9impWg8FNgvstnuHi7v+6E2ZjyZvBNBGck7VYBA2jqOYrd3/tucN1GbA9HURsXOInyUMBevPTEyzVm/piBKggon6nXQ==</d:digest>"
    + "</SOAP-ENV:Header>"
    + "<S:Body>"
    + "<ns2:requestJob xmlns:ns2=\"http://ws.transporter.upa.pt/\">"
    + "<origin>Lisboa</origin>"
    + "<destination>Beja</destination>"
    + "<price>10</price>"
    + "</ns2:requestJob>"
    + "</S:Body>"
    + "</S:Envelope>";



    /** hello-ws SOAP response message captured with LoggingHandler */
    protected static final String HELLO_SOAP_RESPONSE =
      "<S:Envelope "
      + "xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" "
      + "xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
      + "<SOAP-ENV:Header/>"
      + "<S:Body>"
      + "<S:Fault xmlns:ns3=\"http://www.w3.org/2003/05/soap-envelope\">"
      + "<faultcode>S:Server</faultcode>"
      + "<faultstring>invalid ID</faultstring>"
      + "<detail>"
      + "<ns2:BadJobFault xmlns:ns2=\"http://ws.transporter.upa.pt/\"/>"
      + "</detail>"
      + "</S:Fault>"
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
