package pt.upa.transporter.ws.handler;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;



public class TransporterServerHandler implements SOAPHandler<SOAPMessageContext> {

	public static final String REQUEST_PROPERTY = "my.request.property";
	public static final String RESPONSE_PROPERTY = "my.response.property";

	public static final String REQUEST_HEADER = "myRequestHeader";
	public static final String REQUEST_NS = "urn:example";

	public static final String RESPONSE_HEADER = "myResponseHeader";
	public static final String RESPONSE_NS = REQUEST_NS;

	public static final String CLASS_NAME = TransporterServerHandler.class.getSimpleName();
	public static final String TOKEN = "server-handler";

	private ArrayList<String> nonces = new ArrayList();
	
	
	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
//			// outbound message
//
//			// get token from response context
//			String propertyValue = (String) smc.get(RESPONSE_PROPERTY);
//			System.out.printf("%s received '%s'%n", CLASS_NAME, propertyValue);
//
//			// put token in response SOAP header
//			try {
//				// get SOAP envelope
//				SOAPMessage msg = smc.getMessage();
//				SOAPPart sp = msg.getSOAPPart();
//				SOAPEnvelope se = sp.getEnvelope();
//
//				// add header
//				SOAPHeader sh = se.getHeader();
//				if (sh == null)
//					sh = se.addHeader();
//
//				// add header element (name, namespace prefix, namespace)
//				Name name = se.createName(RESPONSE_HEADER, "e", RESPONSE_NS);
//				SOAPHeaderElement element = sh.addHeaderElement(name);
//
//				// *** #9 ***
//				// add header element value
//				String newValue = propertyValue + "," + TOKEN;
//				element.addTextNode(newValue);
//
//				System.out.printf("%s put token '%s' on response message header%n", CLASS_NAME, TOKEN);
//
//			} catch (SOAPException e) {
//				System.out.printf("Failed to add SOAP header because of %s%n", e);
//			}

		} else {
			// inbound message

			// get token from request SOAP header
			try {
				// get SOAP envelope header
				SOAPMessage msg = smc.getMessage();
				SOAPPart sp = msg.getSOAPPart();
				SOAPEnvelope se = sp.getEnvelope();
				SOAPHeader sh = se.getHeader();
				SOAPBody sb = se.getBody();

				// check header
				if (sh == null) {
					System.out.println("Header not found.");
					return true;
				}


				Name name1 = se.createName("nonce", "n", "http://nonce");
				Iterator it1 = sh.getChildElements(name1);
				
				if (!it1.hasNext()) {
					System.out.printf("Header element  not found");
					return true;
				}
				SOAPElement element1 = (SOAPElement) it1.next();

				String element1Value = element1.getValue();
				
				if(nonces.contains(element1Value))
					return false;
				
				
				
				
				Name name2 = se.createName("digest", "d", "http://digest");
				Iterator it2 = sh.getChildElements(name2);
				
				if (!it2.hasNext()) {
					System.out.printf("Header element  not found");
					return true;
				}
				SOAPElement element2 = (SOAPElement) it2.next();

				String element2Value = element2.getValue();

				
                //convert body to string
				DOMSource source = new DOMSource(sb);
				StringWriter stringResult = new StringWriter();
				TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
				String bodyString = stringResult.toString();
				
				final byte[] plainBytes = bodyString.getBytes();
                
                // get a message digest object using the specified algorithm
        		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

        		System.out.println("Computing digest ...");
        		messageDigest.update(plainBytes);
        		byte[] digest = messageDigest.digest();

        		System.out.println("Digested:");
        		String digested = printBase64Binary(digest);

				if(!digested.equals(element2Value))
					return false;
				
	
				
				
			} catch (SOAPException | TransformerException | TransformerFactoryConfigurationError | NoSuchAlgorithmException e) {
				System.out.printf("Failed to get SOAP header because of %s%n", e);
			}

		}

		return true;
	}

	public boolean handleFault(SOAPMessageContext smc) {
		return true;
	}

	public Set<QName> getHeaders() {
		return null;
	}

	public void close(MessageContext messageContext) {
	}

}

