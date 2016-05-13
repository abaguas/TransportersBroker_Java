package pt.upa.broker.ws;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import pt.upa.transporter.ws.BadJobFault_Exception;
import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.cli.TransporterClient;
import pt.upa.broker.exception.BrokerClientException;
import pt.upa.broker.exception.BrokerServerException;
import pt.upa.broker.exception.CouldNotConvertCertificateException;
import pt.upa.broker.exception.CouldNotVerifyCertificateException;
import pt.upa.broker.exception.InvalidSignedCertificateException;
import pt.upa.broker.exception.UDDIException;
import pt.upa.broker.ws.cli.BrokerClient;
import pt.upa.ca.ws.CertificateException_Exception;
import pt.upa.ca.ws.IOException_Exception;
import pt.upa.ca.ws.cli.CAClient;
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
	
	private int nap = 0;
	private String id = "0";
	private String uddiURL = null;
	private String endpoint = null;
	private String name = null;
	private String searchName = "UpaTransporter%";
	private Map<Transport, String> transports = new HashMap<Transport, String>();
	private CAClient caCli = null;
	private Map<String, PublicKey> keys = new HashMap<String, PublicKey>();
	private BrokerClient brokerClient = null;
	private static final String KEYSTORE_PATH = "src/main/resources/UpaBroker.jks";
	private static final String KEYSTORE_PASS = "ins3cur3";
	private final static String ALIAS = "ca";
	private final static String KEY_PASSWORD = "1nsecure";
	private Timer timer = null;
	private TimerTask timerTask = null;

	
	public BrokerPort (String name, String uddiURL, String endpoint, int nap) {
		this.name = name;
		this.uddiURL = uddiURL;
		this.endpoint = endpoint;
		this.nap = nap;
	}
	
	public void init() throws CertificateException_Exception, IOException_Exception, IOException {
		if (name.equals("UpaBroker")) {
			System.out.println("Press enter when UpaBroker2 is on");
	        System.in.read();
			brokerClient = new BrokerClient(uddiURL, "UpaBroker2");
			brokerClient.iAmAlive("I am alive");
		}
		else {
			System.out.println("Press enter to init the timer");
	        System.in.read();
			
			System.out.println("Iniciei o timer de verificacao");

			timer = new Timer();
			timerTask = new TimerTask() {

				@Override
				public void run() {
					setName("UpaBroker");
					System.out.print("Changed name");
					try {
						UDDINaming uddiNaming = new UDDINaming(getUddiURL());
						uddiNaming.rebind("UpaBroker", getEndpoint());
						System.out.println("Did rebind");
					} catch (JAXRException e) {
						throw new BrokerClientException("Could not rebind");
					}
					
				}
			};
			timer.schedule(timerTask, 5000);
		}
	}
	
	public Collection<String> list () throws BrokerServerException {
    	String uddiURL = getUddiURL();
    	String searchName = getSearchName();
		System.out.printf("Contacting UDDI at %s%n", uddiURL);
		Collection<String> endpointAddress = null;
		caCli = new CAClient(uddiURL);
    	try {
			UDDINaming uddiNaming = new UDDINaming(uddiURL);
	    	System.out.printf("Looking for '%s'%n", searchName);
	        endpointAddress = uddiNaming.list(searchName);
       
	        if (endpointAddress.isEmpty()) {
	        	return endpointAddress;
	        }

	        else {
	            Collection<UDDIRecord> records = uddiNaming.listRecords(searchName);
	        	for (UDDIRecord rec : records) {
	        		String transportName = rec.getOrgName();
	        		System.out.println(rec.getOrgName());

	        		String endpoint = rec.getUrl();
	        		if(!keys.containsKey(transportName)){
	        			String s = null;
						
						s = caCli.getCertificate(transportName);
						
	        			byte[] c = parseBase64Binary(s);
	        			CertificateFactory certFactory = null;
						
						certFactory = CertificateFactory.getInstance("X.509");
	        			
	        			InputStream in = new ByteArrayInputStream(c);
	        			Certificate cert = null;
	        			
						cert = certFactory.generateCertificate(in);
						System.out.println("Printing the certificate");
						System.out.println(cert);
						
						if(verifySignedCertificate(cert)){
							PublicKey pk = cert.getPublicKey();
							keys.put(endpoint, pk);
							System.out.println("validei");
							System.out.println(pk);
						}
						else throw new InvalidSignedCertificateException();
	        		}
	        	}
	        }
    	} catch (JAXRException je) {
    		throw new UDDIException();
		} catch (IOException_Exception | CertificateException_Exception | CertificateException je){
			throw new CouldNotConvertCertificateException();
		}
        return endpointAddress;
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
	
	public static boolean verifySignedCertificate(Certificate certificate) throws CouldNotVerifyCertificateException  {
		try {
			System.out.println("Vou buscar a public key da CA");
			PublicKey pk = getCAPublicKey();
			System.out.println("Vou verifcar o certificado com a public key que fui buscar");
			certificate.verify(pk);
		} catch (InvalidKeyException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException
				| SignatureException e) {
			System.out.println("Falhei na validação");
			return false;
		} catch (Exception ee){
			throw new CouldNotVerifyCertificateException();
		}
		return true;
	}
	
	public static PublicKey getCAPublicKey() throws Exception {

		KeyStore keystore;
		try {
			System.out.println("A ler o keystore");
			keystore = readKeystoreFile();
		} catch (Exception e) {
			System.out.println("Could not read keystore");
			throw e;
		}
		PublicKey key = null;
		try{
			System.out.println("A sacar ganda key");
			key = keystore.getCertificate(ALIAS).getPublicKey();
		}catch (Exception e){
			System.out.println("Could not get key");
			throw e;
		}
		return key;
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
	
//////////////////////////////////////////////////////////////////////////////////////////
          //                            OPERATIONS                          //	
//////////////////////////////////////////////////////////////////////////////////////////
	
	
	@Override
	public String ping(String name) {
		nap(getNap());
		TransporterClient tc = null;
		Collection<String> list = list();
		
		if (list.isEmpty()) return null;
		
		for (String endpointURL: list){
			tc = new TransporterClient(endpointURL);
			if (tc.ping(name)==null){
				return null;
			}
		}
		return "OK";
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {

		nap(getNap());
		Collection<String> endpoints = null;
		Map<JobView, String> jobViews = new HashMap<JobView, String>();
		Transport t = new Transport(origin, destination); 
		TransporterClient tc = null;
		
		try {
			JobView jv = null;
			endpoints = list();
			for (String endpoint : endpoints) {
				tc = new TransporterClient(endpoint);
				jv = tc.requestJob(origin, destination, price);
				if (jv!=null){
					jobViews.put(jv, endpoint);
				}
			}		
		} catch (BadLocationFault_Exception e) {
			t.setState("FAILED");
			t.setIdentifier(idFactory());
			UnknownLocationFault ulf = new UnknownLocationFault();
			ulf.setLocation(e.getFaultInfo().getLocation());
			throw new UnknownLocationFault_Exception(e.getMessage(), ulf);
		
		} catch (BadPriceFault_Exception e) {
			t.setState("FAILED");
			t.setIdentifier(idFactory());
			InvalidPriceFault ipf = new InvalidPriceFault();
			ipf.setPrice(e.getFaultInfo().getPrice());
			throw new InvalidPriceFault_Exception(e.getMessage(), ipf);

		} 

		if(jobViews.isEmpty()) {
			t.setState("FAILED");
			t.setIdentifier(idFactory());
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
			System.out.println(j.getCompanyName());
			System.out.println(j.getJobDestination());
			System.out.println(j.getJobOrigin());
			System.out.println(j.getJobIdentifier());
			System.out.println(j.getJobPrice());
			System.out.println(j.getJobState().value());
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
		String endpoint = jobViews.get(budgetedJob);
        transports.put(t, endpoint);
        
        decideJob(jvs, jobViews, budgetedJob, t);
        if (brokerClient != null) {
        	brokerClient.updateTransport(t.createTransportView(), endpoint);
        }
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
		
		nap(getNap());
		Transport transport = getTransportById(id);
		TransporterClient tc=null;	
		String endpoint = transports.get(transport);

		System.out.println("Estou a ir ao endpoint: " + endpoint);
		
		tc = new TransporterClient(endpoint);
		
		JobView jv = tc.jobStatus(id);
		
		System.out.println(jv.getCompanyName());
		System.out.println(jv.getJobDestination());
		System.out.println(jv.getJobOrigin());
		System.out.println(jv.getJobIdentifier());
		System.out.println(jv.getJobPrice());
		System.out.println(jv.getJobState().value());
		
		JobStateView jsv = jv.getJobState();
			
		String state = viewToState(jsv);
		if (!state.equals("ACCEPTED") && !state.equals("ACCEPTED")){
			transport.setState(state);	
		}
		
		TransportView tv = transport.createTransportView();
		
		return tv;
		
	}

	@Override
	public ArrayList<TransportView> listTransports() {
		ArrayList<TransportView> transportViews = new ArrayList<TransportView>();
		Collection<Transport> transps = transports.keySet();
		
		nap(getNap());
		List<Transport> list = new ArrayList<Transport>(transps);
		
		for (Transport transport: list) {
			if (!transport.getIdentifier().contains("t")) {
		        try {
					transportViews.add(viewTransport(transport.getIdentifier()));
				} catch (UnknownTransportFault_Exception e) {
					System.out.println("Cosmic ray exception");
					//never happens because the transporter id came from the transports map
				}
			}
	    }
		return transportViews;
	}

	@Override
	public void clearTransports() {
		
		nap(getNap());
		TransporterClient tc = null;
		Collection<String> clientEndpoints = transports.values();
		
		for (String endpoint: clientEndpoints){
			tc = new TransporterClient(endpoint);
			tc.clearJobs();
		}
		
		brokerClient.clearTransports();
		transports.clear();
	}
    
	
	@Override
	public void updateTransport(TransportView transportView, String endpoint) {
		updateTransportList(transportView, endpoint);
	}

	@Override
	public void iAmAlive(String iAmAlive) {
		if (!getName().equals("UpaBroker")) {
			System.out.println("Receveived I am alive");
			if (timerTask!=null) {
				if (timerTask.cancel()){
					System.out.println("Cancelled timer");
				}
				timerTask = new TimerTask() {
	
					@Override
					public void run() {
						setName("UpaBroker");
						System.out.print("Changed my name");
						try {
							UDDINaming uddiNaming = new UDDINaming(getUddiURL());
							uddiNaming.rebind("UpaBroker", getEndpoint());
							System.out.println("Rebinded");
						} catch (JAXRException e) {
							throw new BrokerClientException("Could not rebing");
						}
						
					}
				};
	
				timer.schedule(timerTask, 5000);
				System.out.println("Timer re-scheduled");
			}
		}
	}
	

//////////////////////////////////////////////////////////////////////////////////////////
       //                          AUXILIAR METHODS                          //	
//////////////////////////////////////////////////////////////////////////////////////////

	
	
	public void updateTransportList(TransportView transportView, String endpoint){
		String id = transportView.getId();
		Collection<Transport> transps = transports.keySet();
		boolean notInList = true;
		for (Transport transport: transps) {
			if (transport.getIdentifier().equals(id)) {
				transport.setState(transportView.getState().value());
				notInList = false;
			}
		}
		if (notInList){
			transports.put(new Transport(transportView), endpoint);
		}
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
	
	
	public String idFactory(){
		String id = getId();
		int i = Integer.parseInt(id);
		i++;
		
		id = Integer.toString(i);
		setId(id);
		
		return "f".concat(id);
	}
	
	private void nap(int seconds) {
        try {
            System.out.printf("%s %s>%n    ", Thread.currentThread(), this);
            System.out.printf("Sleeping for %d seconds...%n", seconds);

            Thread.sleep(seconds*1000);

            System.out.printf("%s %s>%n    ", Thread.currentThread(), this);
            System.out.printf("Woke up!%n");

        } catch(InterruptedException e) {
            System.out.printf("%s %s>%n    ", Thread.currentThread(), this);
            System.out.printf("Caught exception: %s%n", e);
        }
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

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public int getNap() {
		return nap;
	}

}