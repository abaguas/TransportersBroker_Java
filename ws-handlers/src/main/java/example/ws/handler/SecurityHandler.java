package example.ws.handler;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;




public class SecurityHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public Set<QName> getHeaders() {
		return null;
	}
	
	
	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		
		System.out.println("Server: handleMessage()");
		
        Boolean outboundElement = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        
        try {
            if (outboundElement.booleanValue()) {
                System.out.println("Writing header in outbound SOAP message...");

                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();

                // add header
                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();

                
                // add header element (name, namespace prefix, namespace)
                Name headerName = se.createName("nonce", "n", "http://nonce");
                SOAPHeaderElement headerElement = sh.addHeaderElement(headerName);

                
                // add header element value
        		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        		System.out.println(random.getProvider().getInfo());
        		System.out.println("Generating random byte array ...");
        		final byte array[] = new byte[16];
        		random.nextBytes(array);
        		System.out.println(printHexBinary(array));
                String valueString = new String(array, "UTF-8");
                System.out.println(valueString);
                headerElement.addTextNode(valueString);
                


                // add body
                SOAPBody sb = se.addBody();
                Name bodyName = se.createName("digest", "d", "http://digest");   
                SOAPBodyElement bodyElement = sb.addBodyElement(bodyName);

                //FIXME getContentToEncrytpt
                String plainText = smc.getMessage().getSOAPPart().getEnvelope().getBody().getValue();
                
                final byte[] plainBytes = plainText.getBytes();

        		System.out.println("Text:");
        		System.out.println(plainText);
        		System.out.println("Bytes:");
        		System.out.println(printHexBinary(plainBytes));

        		// get a message digest object using the specified algorithm
        		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        		System.out.println(messageDigest.getProvider().getInfo());

        		System.out.println("Computing digest ...");
        		messageDigest.update(plainBytes);
        		byte[] digest = messageDigest.digest();

        		System.out.println("Digest:");
        		System.out.println(printHexBinary(digest));

                String bodyString = new String(digest, "UTF-8");
                System.out.println(bodyString);
                
                
                String transp = null;
                
                
                
                bodyElement.addTextNode(bodyString);
                
                
            }
            
            else {
                System.out.println("Reading header in inbound SOAP message...");

                // get SOAP envelope header
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();
                SOAPHeader sh = se.getHeader();

                // check header
                if (sh == null) {
                    System.out.println("Header not found.");
                    return true;
                }
            	
                // get first header element
                Name name = se.createName("receivedNonce", "rn", "http://receivedNonce");
                Iterator it = sh.getChildElements(name);
                // check header element
                if (!it.hasNext()) {
                    System.out.println("Header element not found.");
                    return true;
                }
                SOAPElement element = (SOAPElement) it.next();

                // get header element value
                String recNonce = element.getValue();
                int value = Integer.parseInt(recNonce);

                
            }
            
        } catch (Exception e) {
            System.out.print("Caught exception in handleMessage: ");
            System.out.println(e);
            System.out.println("Continue normal processing...");
        }
        
                
        
        
        
        
		return true;
	}

	
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public void close(MessageContext context) {
				
	}



	
	
}
