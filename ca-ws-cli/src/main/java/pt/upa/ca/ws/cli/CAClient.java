package pt.upa.ca.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.security.cert.Certificate;
import java.util.Map;

import javax.xml.ws.BindingProvider;

// classes generated from WSDL
import pt.upa.ca.ws.CAImplService;
import pt.upa.ca.ws.CertificateException_Exception;
import pt.upa.ca.ws.IOException_Exception;
import pt.upa.ca.ws.CA;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

public class CAClient {
	
	

	public static void main(String[] args) throws Exception {
		// Check arguments
//		if (args.length < 2) {
//			System.err.println("Argument(s) missing!");
//			System.err.printf("Usage: java %s uddiURL name%n", CAClient.class.getName());
//			return;
//		}
//
//		String uddiURL = args[0];
//		String name = args[1];
//
//		System.out.printf("Contacting UDDI at %s%n", uddiURL);
//		UDDINaming uddiNaming = new UDDINaming(uddiURL);
//
//		System.out.printf("Looking for '%s'%n", name);
//		String endpointAddress = uddiNaming.lookup(name);
//
//		if (endpointAddress == null) {
//			System.out.println("Not found!");
//			return;
//		} else {
//			System.out.printf("Found %s%n", endpointAddress);
//		}
//
//		System.out.println("Creating stub ...");
//		CAImplService service = new CAImplService();
//		CA port = service.getCAImplPort();
//
//		System.out.println("Setting endpoint address ...");
//		BindingProvider bindingProvider = (BindingProvider) port;
//		Map<String, Object> requestContext = bindingProvider.getRequestContext();
//		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
//
//		System.out.println("Remote call ...");
//		String result = port.sayHello("friend");
//		System.out.println(result);
	}
	
	public String getCertificate(String name) throws CertificateException_Exception, IOException_Exception{
		CAImplService service = new CAImplService();
		CA port = service.getCAImplPort();
		String c = port.getCertificate(name);
		return c;
	}

}
