package pt.upa.ca.ws;

import java.io.IOException;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.jws.WebService;

import pt.upa.ca.exception.InvalidWebServiceNameException;

@WebService
public interface CA {

	String getCertificate(String name) throws CertificateException, IOException, InvalidWebServiceNameException;

}
