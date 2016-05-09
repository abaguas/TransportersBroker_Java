package pt.upa.ca.ws;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.jws.WebService;

import pt.upa.ca.exception.InvalidWebServiceNameException;

@WebService(endpointInterface = "pt.upa.ca.ws.CA")
public class CAImpl implements CA {
	
	final static String CERTIFICATE_FILES = "/Users/abaguas/Documents/SD/A_64-project/ca-ws/src/main/resources/";
	final static String UPABROKER = "UpaBroker";
	final static String UPATRANSPORTER1 = "UpaTransporter1";
	final static String UPATRANSPORTER4 = "UpaTransporter4";
	final static String CER = ".cer";
	

	public Certificate getCertificate(String name) throws CertificateException, IOException, InvalidWebServiceNameException {
		Certificate certificate = null;
		if (name.equals(UPABROKER)) {
			certificate = readCertificateFile(CERTIFICATE_FILES+UPABROKER+CER);
		}
		else if (name.equals(UPATRANSPORTER1)){
			certificate = readCertificateFile(CERTIFICATE_FILES+UPABROKER+CER);
		}
		else if (name.equals(UPATRANSPORTER4)){
			certificate = readCertificateFile(CERTIFICATE_FILES+UPABROKER+CER);
		}
		else throw new InvalidWebServiceNameException(name);
		
		return certificate;

	}
	
	
	public static Certificate readCertificateFile(String certificateFilePath) throws CertificateException, IOException {
		FileInputStream fis;

		try {
			fis = new FileInputStream(certificateFilePath);
		} catch (FileNotFoundException e) {
			System.err.println("Certificate file <" + certificateFilePath + "> not fount.");
			return null;
		}
		BufferedInputStream bis = new BufferedInputStream(fis);

		CertificateFactory cf = CertificateFactory.getInstance("X.509");

		if (bis.available() > 0) {
			Certificate cert = cf.generateCertificate(bis);
			return cert;
			// It is possible to print the content of the certificate file:
			// System.out.println(cert.toString());
		}
		bis.close();
		fis.close();
		return null;
	}

	
}
