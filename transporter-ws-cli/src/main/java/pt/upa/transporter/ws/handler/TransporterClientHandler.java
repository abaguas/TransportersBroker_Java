package pt.upa.transporter.ws.handler;

import static javax.xml.bind.DatatypeConverter.printHexBinary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.MessageContext.Scope;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.NodeList;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;




public class TransporterClientHandler implements SOAPHandler<SOAPMessageContext> {

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


                //generate secure random number
        		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        		System.out.println("Generating random byte array ...");
        		final byte array[] = new byte[16];
        		random.nextBytes(array);

        		System.out.println(printBase64Binary(array));
        		headerElement.addTextNode(printBase64Binary(array));
 


                // get body
                SOAPBody sb = se.getBody();
//                NodeList nl = sb.getElementsByTagNameNS("http://ws.transporter.upa.pt", "*");
//                String st = nl.item(0).getChildNodes().item(0).getNodeValue();
				
                

                DOMSource source = new DOMSource(sb);
				StringWriter stringResult = new StringWriter();
				TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
				String bodyString = stringResult.toString();
                
                
                //FIXME getContentToEncrytpt
                //String plainText = smc.getMessage().getSOAPPart().getEnvelope().getBody().getValue();
                
                
				/*                
                MessageFactory factory = MessageFactory.newInstance();
                SOAPMessage newMessage = factory.createMessage(oldMessage.getMimeHeaders(), inputByteArray);
                oldMessage.getSOAPPart().setContent(newMessage.getSOAPPart().getContent());
 				*/                                                
                
				
                final byte[] plainBytes = bodyString.getBytes();



        		// get a message digest object using the specified algorithm
        		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

        		messageDigest.update(plainBytes);
        		byte[] digest = messageDigest.digest();

        		System.out.println("Digest:");

        		
        		
 		   		// add body element (name, namespace prefix, namespace)
                Name bodyName = se.createName("digestedBody", "db", "http://digestedBody");
                SOAPBodyElement bodyElement = sb.addBodyElement(bodyName);
                

                System.out.println(printBase64Binary(digest));
        		System.out.println("Digested!!!");

        		Name paramName = se.createName("digest", "d", "http://digest");
//        		SOAPBodyElement service = body.addBodyElement(serviceName);   
        		SOAPHeaderElement param = sh.addHeaderElement(paramName);
        		param.addTextNode(printBase64Binary(digest));
        		
//        		MessageFactory factory = MessageFactory.newInstance();
//        		SOAPMessage newMessage = factory.createMessage();
//        		
//        		
//                SOAPPart spn = newMessage.getSOAPPart();
//                SOAPEnvelope sen = spn.getEnvelope();
//
//                // add header
//                SOAPHeader shn = sen.getHeader();
//                if (shn == null)
//                    shn = sen.addHeader();
                

        		
//        		Name serviceName = se.createName("digestedBody", "db", "http://getspeech");
        		
        		

                
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


	public static KeyStore readKeystoreFile(String keyStoreFilePath, char[] keyStorePassword) throws Exception {
		FileInputStream fis;
		try {
			fis = new FileInputStream(keyStoreFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Keystore file <" + keyStoreFilePath + "> not found.");
			return null;
		}
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(fis, keyStorePassword);
		return keystore;
	}
	
	
}

