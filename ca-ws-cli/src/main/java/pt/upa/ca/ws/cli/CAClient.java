package pt.upa.ca.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.security.cert.Certificate;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

// classes generated from WSDL
import pt.upa.ca.ws.CAImplService;
import pt.upa.ca.ws.CertificateException_Exception;
import pt.upa.ca.ws.IOException_Exception;
import pt.upa.ca.exception.CAClientException;
import pt.upa.ca.ws.CA;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

public class CAClient {
	
	private CA port = null;
	
	public CAClient(String uddiURL) throws CAClientException {
        final String name = "CA";
        String endpointAddress = null;
		try {
			System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = new UDDINaming(uddiURL);
			System.out.printf("Looking for '%s'%n", name);
			endpointAddress = uddiNaming.lookup(name);
		} catch (Exception e) {
			String msg = String.format("CA Client failed lookup on UDDI at %s!", uddiURL);
			throw new CAClientException(msg, e);
		}
		if (endpointAddress == null) {
			System.out.println("Not found!");
			return;
		} else {
			System.out.printf("Found %s%n", endpointAddress);
		}

		System.out.println("Creating stub ...");
		CAImplService service = new CAImplService();
		port = service.getCAImplPort();

		System.out.println("Setting endpoint address ...");
		BindingProvider bindingProvider = (BindingProvider) port;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
	}
	
	public String getCertificate(String name) throws CertificateException_Exception, IOException_Exception{
		String c = port.getCertificate(name);
		return c;
	}

}
