package pt.upa.transporter.ws.handler;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
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
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import pt.upa.ca.ws.CertificateException_Exception;
import pt.upa.ca.ws.IOException_Exception;
import pt.upa.ca.ws.cli.CAClient;
import pt.upa.transporter.exception.CouldNotConvertCertificateException;
import pt.upa.transporter.exception.CouldNotVerifyCertificateException;
import pt.upa.transporter.exception.InvalidSignedCertificateException;



public class TransporterServerHandler implements SOAPHandler<SOAPMessageContext> {


	private ArrayList<String> nonces = new ArrayList<String>();
	public static CAClient ca = new CAClient("http://localhost:9090"); //FIXME TODO XXX
	private static String TransporterName = null;
	private static final String KEYSTORE_PATH = "src/main/resources/";
	private static final String KEYSTORE_PASS = "ins3cur3";
	private final static String KEY_PASSWORD = "1nsecure";
	
	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			try {
               
            	
                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();

                // add header
                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();
               
                System.out.println("Creating header element \"transporter\" with information from Broker...");
        		Name transpName = se.createName("transporter", "t", "http://transporter"); 
        		SOAPHeaderElement transp = sh.addHeaderElement(transpName);
        		
        		if(TransporterName!=null)
        		transp.addTextNode(TransporterName);
                
                // add header element (name, namespace prefix, namespace)
        		System.out.println("Creating header element \"nonce\"...");
                Name headerName = se.createName("nonce", "n", "http://nonce");
                SOAPHeaderElement headerElement = sh.addHeaderElement(headerName);


                //generate secure random number
        		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        		final byte[] nonce= new byte[16];
        		random.nextBytes(nonce);

        		headerElement.addTextNode(printBase64Binary(nonce));
 
                SOAPBody sb = se.getBody();
                
                DOMSource source = new DOMSource(sb);
				StringWriter stringResult = new StringWriter();
				TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
				String bodyString = stringResult.toString();                                         
                	
				
                final byte[] plainBytes = bodyString.getBytes();
                
                System.out.println("Creating digest from message body...");

        		// get a message digest object using the specified algorithm
        		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

        		messageDigest.update(plainBytes);
        		byte[] digest = messageDigest.digest();

                System.out.println("Creating header element \"digest\"...");
        		Name paramName = se.createName("digest", "d", "http://digest"); 
        		SOAPHeaderElement param = sh.addHeaderElement(paramName);
        		
        		PrivateKey pk = null;
                try {
	                System.out.println("Getting private key from keystore...");
					pk = getPrivateKeyFromKeystore();
				} catch (Exception e) {
					System.out.println("[ERROR] Cannot get private key from keystore!");
					e.printStackTrace();
				}
                System.out.println("Concatenating digest with nonce...");
                byte[] digestWithNonce = parseBase64Binary(printBase64Binary(digest)+printBase64Binary(nonce));
                try {
	                System.out.println("Making digital signature to digest+nonce...");
					digestWithNonce = makeDigitalSignature(digestWithNonce, pk);
				} catch (Exception e) {
					System.out.println("[ERROR] Cannot make digital signature!");
					e.printStackTrace();
				}
        		
                String digested = printBase64Binary(digestWithNonce);
              
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

				System.out.println("Reading \"transporter\" element from header...");
				Name name3 = se.createName("transporter", "t", "http://transporter");
				Iterator it3 = sh.getChildElements(name3);

				if (!it3.hasNext()) {
					System.out.printf("Header element  not found");
					return true;
				}
				SOAPElement transporterNameResponse = (SOAPElement) it3.next();

				TransporterName = transporterNameResponse.getValue();
				
				
				System.out.println("Reading \"nonce\" element from header...");
				Name name1 = se.createName("nonce", "n", "http://nonce");
				Iterator it1 = sh.getChildElements(name1);
				
				if (!it1.hasNext()) {
					System.out.printf("Header element  not found");
					return true;
				}
				SOAPElement nonceResponse = (SOAPElement) it1.next();

				String nonceResponseValue = nonceResponse.getValue();
				
				if(nonces.contains(nonceResponseValue)){
    				System.out.println("[ERROR] Nonce already exists!");

					return false;
				}
				
				System.out.println("Reading \"digest\" element from header...");
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
                
				System.out.println("Making digest from message body...");
        		MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");

        		messageDigest.update(plainBytes);
        		byte[] digest = messageDigest.digest();

        		
        		PublicKey pk = null;
            	try {
    				System.out.println("Getting CA certificate from CAClient...");

            		String s = ca.getCertificate("UpaBroker");
				
            		byte[] c = parseBase64Binary(s);
            		CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

            		InputStream in = new ByteArrayInputStream(c);
            		Certificate cert = certFactory.generateCertificate(in);

            		
            		
            		if(verifySignedCertificate(cert)){
            			pk = cert.getPublicKey();
            			System.out.println("Certificate verification successful!");
            		}
            		else{
            			System.out.println("[ERROR] Certificate verification failed!");
            			throw new InvalidSignedCertificateException();
            		}
        	        
            	} catch (IOException_Exception | CertificateException_Exception | CertificateException je){

        			throw new CouldNotConvertCertificateException();
        		}
	        		
            	System.out.println("Concatenating generated digest with received nonce...");
            	digest = parseBase64Binary(printBase64Binary(digest)+nonceResponseValue);

        		try {
					boolean verified = verifyDigitalSignature(parseBase64Binary(digestResponseValue), digest, pk);
					if (!verified){
						System.out.println("[ERROR] Digital signature verification failed!");
						return false;
					}
					
					System.out.println("Digital signature verification successful!");
				} catch (Exception e) {
					e.printStackTrace();
				}				
				
				
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

	
	public static KeyStore readKeystoreFile() throws Exception{
		FileInputStream fis;
		try {
			fis = new FileInputStream(KEYSTORE_PATH+TransporterName+".jks");
		} catch (FileNotFoundException e) {
			System.err.println("Keystore file <" + KEYSTORE_PATH + TransporterName+".jks" + "> not found.");
			return null;
		}
		KeyStore keystore;
		try {
			keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (KeyStoreException e) {
			System.out.println("could not get an instance of keystore");
			throw e;
		}
		try {
			keystore.load(fis, KEYSTORE_PASS.toCharArray());
		} catch (NoSuchAlgorithmException | CertificateException | IOException e) {
			System.out.println("could not load keystore");
			throw e;
		}
		return keystore;
	}
	
	
	public static PrivateKey getPrivateKeyFromKeystore() throws Exception {

		KeyStore keystore;
		try {
			keystore = readKeystoreFile();
		} catch (Exception e) {
			System.out.println("Could not read keystore");
			throw e;
		}
		PrivateKey key = null;
		try{
			key = (PrivateKey) keystore.getKey(TransporterName, KEY_PASSWORD.toCharArray());
		}catch (Exception e){
			System.out.println("Could not get key");
			throw e;
		}
				

		return key;
	}
	public static byte[] makeDigitalSignature(byte[] bytes, PrivateKey privateKey) throws Exception {

		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(privateKey);
		sig.update(bytes);
		byte[] signature = sig.sign();

		return signature;
	}
	
	public static boolean verifySignedCertificate(Certificate certificateFile) throws CouldNotVerifyCertificateException  {
		try {
			String caCertString = ca.getCertificate("CA");
			
			byte[] c = parseBase64Binary(caCertString);
    		CertificateFactory certFactory = null;

    		certFactory = CertificateFactory.getInstance("X.509");

    		InputStream in = new ByteArrayInputStream(c);
    		
    		Certificate caCertificate = certFactory.generateCertificate(in);

			PublicKey pk = caCertificate.getPublicKey();
			
			
			certificateFile.verify(pk);
			
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
			System.out.println("[ERROR] Certificate verification failed!");

			return false;
		} catch (Exception ee){
			throw new CouldNotVerifyCertificateException();
		}
		return true;
	}
	
}

