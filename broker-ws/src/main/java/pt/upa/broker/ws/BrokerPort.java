package pt.upa.broker.ws;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.broker.exception.InvalidSignedCertificateException;
import pt.upa.broker.ws.cli.BrokerClient;
import pt.upa.ca.ws.CA;
import pt.upa.ca.ws.CertificateException_Exception;
import pt.upa.ca.ws.IOException_Exception;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDIRecord;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import javax.jws.WebService;
import javax.xml.registry.JAXRException;

@WebService(
	    endpointInterface="pt.upa.broker.ws.BrokerPortType",
	    wsdlLocation="broker.3_0.wsdl",
	    name="BrokerWebService",
	    portName="BrokerPort",
	    targetNamespace="http://ws.broker.upa.pt/",
	    serviceName="BrokerService"
)

public class BrokerPort implements BrokerPortType {
	
	private String id = "0";
	private String uddiURL = null;
	private String name = null;
	private String searchName = "UpaTransporter%";
	private Map<Transport, String> transports = new HashMap<Transport, String>();
	private CA ca = null;
	private Map<String, PublicKey> keys = new HashMap<String, PublicKey>();
	private BrokerClient brokerClient = null;
	private static final String KEYSTORE_PATH = "src/main/resources/UpaBroker.jks";
	private static final String KEYSTORE_PASS = "1nsecure";
	private final static String KEY_ALIAS = "example";
	private final static String KEY_PASSWORD = "ins3cur3";

	
	public BrokerPort (String name, String uddiURL) {
		this.name = name;
		this.uddiURL = uddiURL;
		if (name.equals("UpaBroker")){
			
		}
	}
	
	public Collection<String> list () throws JAXRException {
    	String uddiURL = getUddiURL();
    	String searchName = getSearchName();
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
    	UDDINaming uddiNaming = new UDDINaming(uddiURL);
    	System.out.printf("Looking for '%s'%n", searchName);
        Collection<String> endpointAddress = uddiNaming.list(searchName);
       
        
        if (endpointAddress.isEmpty()) {
            System.out.println("Not found!");
            return null;
        } 
     
        else {
            Collection<UDDIRecord> record = uddiNaming.listRecords(searchName);
            
//        	for (UDDIRecord rec : record){
//        		String transportName = rec.getOrgName();
//        		String endpoint = rec.getUrl();
//        		if(!keys.containsKey(transportName)){
//        			String s = null;
//					
//        			try {
//						s = (ca.getCertificate(transportName));
//					} catch (CertificateException_Exception | IOException_Exception e1) {
//						e1.printStackTrace();
//					}
//					
//        			byte[] c = parseBase64Binary(s);
//        			CertificateFactory certFactory = null;
//					
//        			try {
//						certFactory = CertificateFactory.getInstance("X.509");
//					} catch (CertificateException e1) {
//						e1.printStackTrace();
//					}
//        			
//        			InputStream in = new ByteArrayInputStream(c);
//        			Certificate cert = null;
//        			
//        			try {
//						cert = certFactory.generateCertificate(in);
//					} catch (CertificateException e) {
//						e.printStackTrace();
//					}
//        			try {
//						if(verifySignedCertificate(cert)){
//							PublicKey pk = cert.getPublicKey();
//							keys.put(endpoint, pk);
//						}
//						else{
//							throw new InvalidSignedCertificateException();
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//        		}
//        	}
            return endpointAddress;
        }
        
    }
	
	
	public String lookUp (String name) throws JAXRException {
		String uddiURL = getUddiURL();
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
    	UDDINaming uddiNaming = new UDDINaming(uddiURL);
    	System.out.printf("Looking for '%s'%n", name);
        String endpointAddress = uddiNaming.lookup(name);
        
        if (endpointAddress == null) {
            System.out.println("Not found!");
            return null;
        } else {
            return endpointAddress;
        }
    }
	
	public static boolean verifySignedCertificate(Certificate certificate) throws Exception {
		
			try {
				PublicKey pk = getCAPublicKey();
				certificate.verify(pk);
			} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
					| SignatureException e) {
				e.printStackTrace();
			}

		return true;
	}
	
	public static PublicKey getCAPublicKey() throws Exception {

		KeyStore keystore = readKeystoreFile();
		PublicKey key = (PublicKey) keystore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());

		return key;
	}
	
	public static KeyStore readKeystoreFile() throws Exception {
		FileInputStream fis;
		try {
			fis = new FileInputStream(KEYSTORE_PATH);
		} catch (FileNotFoundException e) {
			System.err.println("Keystore file <" + KEYSTORE_PATH + "> not found.");
			return null;
		}
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		keystore.load(fis, KEYSTORE_PASS.toCharArray());
		return keystore;
	}
	
//////////////////////////////////////////////////////////////////////////////////////////
          //                            OPERATIONS                          //	
//////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public String ping(String name) {
		try {
			TransporterClient tc = null;
			Collection<String> list = list();
			for (String endpointURL: list){
				tc = new TransporterClient(endpointURL);
				if (tc.ping(name)==null){
					return null;
				}
			}
			return "OK";
		} catch (JAXRException e1) {
			return "Unreachable"; //TODO connection exception
		}
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {

		Collection<String> endpoints = null;
		Map<JobView, String> jobViews = new HashMap<JobView, String>();
		Transport t = new Transport(origin, destination); 
		TransporterClient tc = null;
		
		try {
			JobView jv = null;
			endpoints = list();
			for (String endpoint : endpoints){
				tc = new TransporterClient(endpoint);
				jv = tc.requestJob(origin, destination, price);
				if (jv!=null){
					jobViews.put(jv, endpoint);
				}
			}
		} catch (JAXRException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();			
		} catch (BadLocationFault_Exception e) {
			t.setState("FAILED");
			UnknownLocationFault ulf = new UnknownLocationFault();
			ulf.setLocation(e.getFaultInfo().getLocation());
			throw new UnknownLocationFault_Exception(e.getMessage(), ulf);
		
		} catch (BadPriceFault_Exception e) {
			t.setState("FAILED");
			InvalidPriceFault ipf = new InvalidPriceFault();
			ipf.setPrice(e.getFaultInfo().getPrice());
			throw new InvalidPriceFault_Exception(e.getMessage(), ipf);

		} 

		if(jobViews.isEmpty()){
			System.out.println("Entrei");
			t.setState("FAILED");
			UnavailableTransportFault utf = new UnavailableTransportFault();
			utf.setOrigin(origin);
			utf.setDestination(destination);
			throw new UnavailableTransportFault_Exception("Unavailable transport from origin to destination", utf);
		}
		else{
			return chooseJob(jobViews, price, t);
		}

	}
	
	public String chooseJob (Map<JobView, String> jobViews, int price, Transport t) throws UnavailableTransportPriceFault_Exception{
		Collection<JobView> jvs = jobViews.keySet();
		JobView budgetedJob = null;
		
		for (JobView j : jvs) {
			if (j.getJobPrice()<=price){
				budgetedJob = j;
				price = j.getJobPrice();
			}
		}
		
		if (budgetedJob == null) {
			t.setState("FAILED");
			t.setIdentifier(idFactory());
			UnavailableTransportPriceFault utpf = new UnavailableTransportPriceFault();
			utpf.setBestPriceFound(price);
			throw new UnavailableTransportPriceFault_Exception("Non-existent transport with pretended price",utpf);
		}
		
		t.setCompanyName(budgetedJob.getCompanyName());
		t.setPrice(budgetedJob.getJobPrice());
		t.setState("BUDGETED");
		t.setIdentifier(budgetedJob.getJobIdentifier());
        transports.put(t, jobViews.get(budgetedJob));
        
        decideJob(jvs, jobViews, budgetedJob, t);
        
        return t.getIdentifier();
	}
	
	public void decideJob(Collection<JobView> jvs, Map<JobView, String> jobViews, JobView budgetedJob, Transport t){
		TransporterClient tc = null;
		
		for (JobView j: jvs) {
			
			tc = new TransporterClient(jobViews.get(j));

			if (j.equals(budgetedJob)){
				try {
					tc.decideJob(t.getIdentifier(), true);
					t.setState("BOOKED");
				} catch (BadJobFault_Exception e) {
					t.setState("FAILED");
				}
			}
			else {
				try {
					tc.decideJob(t.getIdentifier(), false);  //XXX se der mal foi aqui (alterei o identificador)
					t.setState("FAILED");
				} catch (BadJobFault_Exception e) {
					t.setState("FAILED");
				}
			}
			
		}
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		Transport transport = getTransportById(id);
		TransporterClient tc=null;	

		tc = new TransporterClient(transports.get(transport));
		
		JobView jv = tc.jobStatus(id);
		
		JobStateView jsv = jv.getJobState();
			
		String state = viewToState(jsv);
		if (!state.equals("ACCEPTED") && !state.equals("ACCEPTED")){
			transport.setState(state);	
		}
		return transport.createTransportView();
		
	}

	@Override
	public ArrayList<TransportView> listTransports() {
		ArrayList<TransportView> transportViews = new ArrayList<TransportView>();
		Collection<Transport> transps = transports.keySet();
		
		List<Transport> list = new ArrayList<Transport>(transps);
		
		for (Transport transport: list) {
	        try {
				transportViews.add(viewTransport(transport.getIdentifier()));
			} catch (UnknownTransportFault_Exception e) {
				System.out.println("Cosmic ray exception");
				//never happens because the transporter id came from the transports map
			}
	    }
		return transportViews;
	}

	@Override
	public void clearTransports() {
		TransporterClient tc = null;
		Collection<String> clientEndpoints = transports.values();
		
		for (String endpoint: clientEndpoints){
			tc = new TransporterClient(endpoint);
			tc.clearJobs();
		}

		transports.clear();
	}
    
	public Transport getTransportById(String id) throws UnknownTransportFault_Exception{
		if (id != null){
			Collection<Transport> transps = transports.keySet();
			for (Transport t: transps){
				if (id.equals(t.getIdentifier())){
					return t;
				}
			}
		}
		UnknownTransportFault fault = new UnknownTransportFault();
		fault.setId(id);
		throw new UnknownTransportFault_Exception("Unknown id", fault);
	}
	
	public String viewToState(JobStateView view){
		return view.name();
	}
	
	@Override
	public void updateTransport(TransportView transport) {
		if (!getName().equals("UpaBroker")){
			//TODO atualizar o estado
		}
	}

	@Override
	public void iAmAlive(String iAmAlive) {
		if (!getName().equals("UpaBroker")){
			//TODO adiar o timeout
		}
	}
	
	
	public String idFactory(){
		String id = getId();
		int i = Integer.parseInt(id);
		i++;
		
		id = Integer.toString(i);
		setId(id);
		
		return "f".concat(id);
	}
	
//////////////////////////////////////////////////////////////////////////////////////////
      //                           GETTERS AND SETTERS                          //	
//////////////////////////////////////////////////////////////////////////////////////////

	
	public String getUddiURL() {
		return uddiURL;
	}

	public void setUddiURL(String uddiURL) {
		this.uddiURL = uddiURL;
	}

	public String getSearchName() {
		return searchName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
