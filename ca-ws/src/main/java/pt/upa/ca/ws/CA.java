package pt.upa.ca.ws;

import java.security.PublicKey;
import java.security.cert.Certificate;

import javax.jws.WebService;

@WebService
public interface CA {

	Certificate getCertificate(String name);

}
