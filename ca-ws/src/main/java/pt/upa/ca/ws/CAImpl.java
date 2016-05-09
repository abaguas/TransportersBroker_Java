package pt.upa.ca.ws;

import java.security.PublicKey;
import java.security.cert.Certificate;

import javax.jws.WebService;

@WebService(endpointInterface = "pt.upa.ca.ws.CA")
public class CAImpl implements CA {

	public Certificate getCertificate(String name) {
		return null;
	}
	
}
