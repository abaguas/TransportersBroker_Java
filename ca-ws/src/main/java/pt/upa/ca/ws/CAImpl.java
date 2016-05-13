package pt.upa.ca.ws;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import static javax.xml.bind.DatatypeConverter.printBase64Binary;


import javax.jws.WebService;

import pt.upa.ca.exception.InvalidWebServiceNameException;

@WebService(endpointInterface = "pt.upa.ca.ws.CA")
public class CAImpl implements CA {
	
	final static String CERTIFICATE_FILES = "src/main/resources/";
	//"../../../../../resources/";
	final static String UPABROKER = "UpaBroker";
	final static String UPATRANSPORTER1 = "UpaTransporter1";
	final static String UPATRANSPORTER4 = "UpaTransporter4";
	final static String CER = ".cer";
	final static String CA_CERTIFICATE = "ca-certificate.pem.txt";
	final static String CA = "CA";
	

	public String getCertificate(String name) throws CertificateException, IOException, InvalidWebServiceNameException {
		Certificate certificate = null;
		if (name.equals(UPABROKER)) {
			certificate = readCertificateFile(CERTIFICATE_FILES+UPABROKER+CER);
		}
		else if (name.equals(UPATRANSPORTER1)){
			certificate = readCertificateFile(CERTIFICATE_FILES+UPATRANSPORTER1+CER);
		}
		else if (name.equals(UPATRANSPORTER4)){
			certificate = readCertificateFile(CERTIFICATE_FILES+UPATRANSPORTER4+CER);
		}
		else if (name.equals(CA)){
			certificate = readCertificateFile(CERTIFICATE_FILES+CA_CERTIFICATE);
		}
		else {
			System.out.println("Dont have the certificate from this transporter: "+ name);
			return null;
		}
		
		
		System.out.println("Estou a enviar o ganda certificado" + name);
		return printBase64Binary(certificate.getEncoded());

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
