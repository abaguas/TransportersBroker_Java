package pt.upa.transporter.ws.handler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
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
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.upa.ca.ws.cli.CAClient;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;




public class TransporterClientHandler implements SOAPHandler<SOAPMessageContext> {


	private ArrayList<String> nonces = new ArrayList();
	private CAClient ca;
	private static final String KEYSTORE_PATH = "/home/zacarias/SD/proj/A_64-project/broker-ws/src/main/resources/UpaBroker.jks";
	private static final String KEYSTORE_PASS = "ins3cur3";
	private final static String ALIAS = "upabroker";
	private final static String KEY_PASSWORD = "1nsecure";
	
	@Override
	public Set<QName> getHeaders() {
		return null;
	}
	
	
	@Override
	public boolean handleMessage(SOAPMessageContext smc) {
		
		System.out.println("Server: handleMessage()");
		
        Boolean outboundElement = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		String TransporterName = (String) smc.get("Transporter");
	
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA "+ TransporterName);
            if (outboundElement.booleanValue()) {
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
	                
	        		Name transpName = se.createName("transporter", "t", "http://transporter"); 
	        		SOAPHeaderElement transp = sh.addHeaderElement(transpName);
	        		transp.addTextNode(TransporterName);
	                
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
	                
					
					

	                
	                //FIXME getContentToEncrypt
	
	                
	                
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

	
	        		Name paramName = se.createName("digest", "d", "http://digest"); 
	        		SOAPHeaderElement param = sh.addHeaderElement(paramName);
	        		
	        		PrivateKey pk = null;
	                try {
						pk = getPrivateKeyFromKeystore();
						System.out.println("----"+printBase64Binary(pk.getEncoded())+"-------------------------------------");
					} catch (Exception e) {
						System.out.println("NAO DEU PARA OBTER A PK");
						e.printStackTrace();
					}
	                
	                try {
						digest = makeDigitalSignature(digest, pk);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        		
	                String digested = printBase64Binary(digest);
	        		System.out.println("Digest:");
	                System.out.println(digested);
	            
	        		System.out.println("Digested!!!");	                
	        		param.addTextNode(digested);
            	}
        		catch (SOAPException | TransformerException | TransformerFactoryConfigurationError | NoSuchAlgorithmException e) {
    				System.out.printf("Failed to get SOAP header because of %s%n", e);
    			}
            } 
        		

            
            
            else {
            	
//    			try {
//    				// get SOAP envelope header
//    				SOAPMessage msg = smc.getMessage();
//    				SOAPPart sp = msg.getSOAPPart();
//    				SOAPEnvelope se = sp.getEnvelope();
//    				SOAPHeader sh = se.getHeader();
//    				SOAPBody sb = se.getBody();
//
//    				// check header
//    				if (sh == null) {
//    					System.out.println("Header not found.");
//    					return true;
//    				}
//
//
//    				Name name1 = se.createName("nonce", "n", "http://nonce");
//    				Iterator it1 = sh.getChildElements(name1);
//    				
//    				if (!it1.hasNext()) {
//    					System.out.printf("Header element  not found");
//    					return true;
//    				}
//    				SOAPElement nonceResponse = (SOAPElement) it1.next();
//
//    				String nonceResponseValue = nonceResponse.getValue();
//    				
//    				if(nonces.contains(nonceResponseValue))
//    					return false;
////    				System.out.println("NONCE 35 " + nonceResponseValue + "----------------------------------------");
//
//    				
//    				
//    				
//    				Name name2 = se.createName("digest", "d", "http://digest");
//    				Iterator it2 = sh.getChildElements(name2);
//    				
//    				if (!it2.hasNext()) {
//    					System.out.printf("Header element  not found");
//    					return true;
//    				}
//    				SOAPElement digestResponse = (SOAPElement) it2.next();
//
//    				String digestResponseValue = digestResponse.getValue();
//
//    				
//                    //convert body to string
//    				DOMSource source = new DOMSource(sb);
//    				StringWriter stringResult = new StringWriter();
//    				TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
//    				String bodyString = stringResult.toString();
//    				
//    				final byte[] plainBytes = bodyString.getBytes();
//                    
//                    // get a message digest object using the specified algorithm
//            		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
//
//            		System.out.println("Computing digest ...");
//            		messageDigest.update(plainBytes);
//            		byte[] digest = messageDigest.digest();
//
//            		System.out.println("Digested:");
//            		String digested = printBase64Binary(digest);
//
//    				if(!digested.equals(digestResponseValue))
//    					return false;
////    				System.out.println("BENFICAAAAA 35 " + digestResponseValue + "----------------------------------------" +digested);
//    	
//    				
//    				
//    			} catch (SOAPException | TransformerException | TransformerFactoryConfigurationError | NoSuchAlgorithmException e) {
//    				System.out.printf("Failed to get SOAP header because of %s%n", e);
//    			}
        
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
	
	public static KeyStore readKeystoreFile() throws Exception{
		FileInputStream fis;
		try {
			fis = new FileInputStream(KEYSTORE_PATH);
		} catch (FileNotFoundException e) {
			System.err.println("Keystore file <" + KEYSTORE_PATH + "> not found.");
			return null;
		}
		KeyStore keystore;
		try {
			keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			System.out.println("Já tenho uma instancia do keystore");
		} catch (KeyStoreException e) {
			System.out.println("could not get an instance of keysore");
			throw e;
		}
		try {
			keystore.load(fis, KEYSTORE_PASS.toCharArray());
			System.out.println("Já loadei a keystore");
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			System.out.println("could not load keystore");
			throw e;
		}
		return keystore;
	}
	
	
	public static PrivateKey getPrivateKeyFromKeystore() throws Exception {

		KeyStore keystore;
		try {
			System.out.println("A ler o keystore");
			keystore = readKeystoreFile();
		} catch (Exception e) {
			System.out.println("Could not read keystore");
			throw e;
		}
		PrivateKey key = null;
		try{
			System.out.println("A sacar ganda key");
			key = (PrivateKey) keystore.getKey(ALIAS, KEY_PASSWORD.toCharArray());
		}catch (Exception e){
			System.out.println("Could not get key");
			throw e;
		}
				

		return key;
	}
	public static byte[] makeDigitalSignature(byte[] bytes, PrivateKey privateKey) throws Exception {

		// get a signature object using the SHA-1 and RSA combo
		// and sign the plain-text with the private key
		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(privateKey);
		sig.update(bytes);
		byte[] signature = sig.sign();

		return signature;
	}

	
}

