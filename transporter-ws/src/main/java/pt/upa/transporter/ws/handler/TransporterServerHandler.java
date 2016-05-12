package pt.upa.transporter.ws.handler;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
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

import pt.upa.ca.ws.CertificateException_Exception;
import pt.upa.ca.ws.IOException_Exception;
import pt.upa.ca.ws.cli.CAClient;



public class TransporterServerHandler implements SOAPHandler<SOAPMessageContext> {


	private ArrayList<String> nonces = new ArrayList();
	private CAClient ca;
	private String TransporterName;
	
	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			try {
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
	
	            SOAPBody sb = se.getBody();
	
	
	            DOMSource source = new DOMSource(sb);
				StringWriter stringResult = new StringWriter();
				TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
				String bodyString = stringResult.toString();
	            
	            
	            //FIXME getContentToEncrytpt
	
	            
	            
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
	
	    		
	
	            String digested = printBase64Binary(digest);
	            System.out.println(digested);
	
	    		System.out.println("Digested!!!");
	
	    		Name paramName = se.createName("digest", "d", "http://digest");
	//        		SOAPBodyElement service = body.addBodyElement(serviceName);   
	    		SOAPHeaderElement param = sh.addHeaderElement(paramName);
	    		param.addTextNode(digested);
			}
			catch (SOAPException | TransformerException | TransformerFactoryConfigurationError | NoSuchAlgorithmException e) {
				System.out.printf("Failed to get SOAP header because of %s%n", e);
			}

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
				SOAPElement nonceResponse = (SOAPElement) it1.next();

				String nonceResponseValue = nonceResponse.getValue();
				
				if(nonces.contains(nonceResponseValue))
					return false;
//				System.out.println("NONCE 35 " + nonceResponseValue + "----------------------------------------");

				
				
				
				Name name2 = se.createName("digest", "d", "http://digest");
				Iterator it2 = sh.getChildElements(name2);
				
				if (!it2.hasNext()) {
					System.out.printf("Header element  not found");
					return true;
				}
				SOAPElement digestResponse = (SOAPElement) it2.next();

				String digestResponseValue = digestResponse.getValue();

				
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

        		
        		

				Name name3 = se.createName("transporter", "t", "http://transporter");
				Iterator it3 = sh.getChildElements(name3);
				
				if (!it3.hasNext()) {
					System.out.printf("Header element  not found");
					return true;
				}
				SOAPElement transporterNameResponse = (SOAPElement) it3.next();

				TransporterName = transporterNameResponse.getValue();
        		System.out.println("A transportadora e esta: ------------------"+TransporterName+"*+*+*+*+*+*+*+*");
        		
        		
        		
        		
//        		String s = null;
//				
//				try {
//					s = ca.getCertificate("UpaBroker");
//				} catch (CertificateException_Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException_Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//    			byte[] c = parseBase64Binary(s);
//    			CertificateFactory certFactory = null;
//				
//				try {
//					certFactory = CertificateFactory.getInstance("X.509");
//				} catch (CertificateException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//    			
//    			InputStream in = new ByteArrayInputStream(c);
//    			Certificate cert = null;
//    			
//				try {
//					cert = certFactory.generateCertificate(in);
//				} catch (CertificateException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//        		
//				PublicKey pk = cert.getPublicKey();
//        		System.out.println("*********************************" + pk);
//        		try {
//					boolean b = verifyDigitalSignature(parseBase64Binary(digestResponseValue), digest, pk);
//					if (!b)
//						return false;
//					else
//						System.out.println("TOMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMAAAAAAAAAAAAAAAAAAAAAAA");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
        		
        		
        		
        		
        		
        		
//        		System.out.println("Digested:");
//        		String digested = printBase64Binary(digest);
//
//        		System.out.println("BENFICAAAAA 35 " + digestResponseValue + "----------------------------------------" +digested);
//        		
//				if(!digested.equals(digestResponseValue))
//					return false;
				
				
				
			} catch (SOAPException | TransformerException | TransformerFactoryConfigurationError | NoSuchAlgorithmException e) {
				System.out.printf("Failed to get SOAP header because of %s%n", e);
			}

		}

		return true;
	}
	
	public static boolean verifyDigitalSignature(byte[] cipherDigest, byte[] bytes, PublicKey publicKey)
			throws Exception {

		// verify the signature with the public key
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initVerify(publicKey);
		sig.update(bytes);
		try {
			return sig.verify(cipherDigest);
		} catch (SignatureException se) {
			System.err.println("Caught exception while verifying signature " + se);
			return false;
		}
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

