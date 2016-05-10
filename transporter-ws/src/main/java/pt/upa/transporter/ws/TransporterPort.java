package pt.upa.transporter.ws;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.swing.Timer;
import javax.xml.registry.JAXRException;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.upa.ca.ws.cli.CAClient;
import pt.upa.ca.ws.CertificateException_Exception;
import pt.upa.ca.ws.IOException_Exception;
import pt.upa.transporter.exception.DoesNotOperateException;
import pt.upa.transporter.exception.InvalidIdentifierException;;
import pt.upa.transporter.exception.InvalidSignedCertificateException;
import pt.upa.transporter.exception.NoAvailableIdentifierException;

@WebService(
	endpointInterface="pt.upa.transporter.ws.TransporterPortType",
	wsdlLocation="transporter.1_0.wsdl",
	name="TransporterWebService",
	portName="TransporterPort",
	targetNamespace="http://ws.transporter.upa.pt/",
	serviceName="TransporterService"
)

@HandlerChain(file = "/Transporter-chain.xml")

public class TransporterPort implements TransporterPortType{

	private String id;
	private String boundId;
	private String name;
	private String uddiURL;
	private ArrayList<Job> jobs = new ArrayList<Job>();
	private ArrayList<String> regiaoSul = new ArrayList<String>(
			Arrays.asList("Setúbal", "Évora", "Portalegre", "Beja", "Faro"));
	private ArrayList<String> regiaoCentro = new ArrayList<String>(
			Arrays.asList("Lisboa", "Leiria", "Santarém", "Castelo Branco", "Coimbra", "Aveiro", "Viseu", "Guarda"));
	private ArrayList<String> regiaoNorte = new ArrayList<String>(
			Arrays.asList("Porto", "Braga", "Viana do Castelo", "Vila Real", "Bragança"));
	private PublicKey brokerKey;
	private CAClient ca = null;
	private static final String BROKER_NAME = "UpaBroker";
	private static final String KEYSTORE_PATH = "src/main/resources/UpaBroker.jks";
	private static final String KEYSTORE_PASS = "1nsecure";
	private final static String KEY_ALIAS = "example";
	private final static String KEY_PASSWORD = "ins3cur3";
	
	public TransporterPort (String name, String uddiURL){
		this.name = name;
		int n = numberTransporter(name)*100000;
		id = Integer.toString(n);
		boundId = Integer.toString(n+100000);
		this.uddiURL = uddiURL;
		String s = null;
		ca = new CAClient(uddiURL);
		
		try {
			s = (ca.getCertificate(BROKER_NAME));
		} catch (CertificateException_Exception | IOException_Exception e1) {
			e1.printStackTrace();
		}
		
		byte[] c = parseBase64Binary(s);
		CertificateFactory certFactory = null;
		
		try {
			certFactory = CertificateFactory.getInstance("X.509");
		} catch (CertificateException e1) {
			e1.printStackTrace();
		}
		
		InputStream in = new ByteArrayInputStream(c);
		Certificate cert = null;
		
		try {
			cert = certFactory.generateCertificate(in);
		} catch (CertificateException e) {
			e.printStackTrace();
		}
		try {
			if(verifySignedCertificate(cert)){
				brokerKey = cert.getPublicKey();
			}
			else{
				throw new InvalidSignedCertificateException();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/////////////////////////////////////////REPETIDO/////////////////////////////////////////////////
	
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

/////////////////////////////////////////REPETIDO/////////////////////////////////////////////////
	
	@Override
	public String ping(String name) {
		return this.name;
	}
	
	@Override
	public JobView requestJob(String origin, String destination, int price)
			throws BadLocationFault_Exception, BadPriceFault_Exception {
		if(origin==null || destination == null){
			BadLocationFault blf = new BadLocationFault();
			blf.setLocation(origin);
			throw new BadLocationFault_Exception("Unknown origin", blf);
		}
		
		if(!(regiaoNorte.contains(origin) || regiaoCentro.contains(origin) || regiaoSul.contains(origin))){
			BadLocationFault blf = new BadLocationFault();
			blf.setLocation(origin);
			throw new BadLocationFault_Exception("Unknown origin", blf);
		}
		if(!(regiaoNorte.contains(destination) || regiaoCentro.contains(destination) || regiaoSul.contains(destination))){
			BadLocationFault blf = new BadLocationFault();
			blf.setLocation(destination);
			throw new BadLocationFault_Exception("Unknown destination", blf);
		}
		if(price < 0){
			BadPriceFault bpf = new BadPriceFault();
			bpf.setPrice(price);
			throw new BadPriceFault_Exception("The price must be positive", bpf);
		}
		
		try {
		  operate(origin, destination, getName());
		} catch (DoesNotOperateException dnoe){
			return null;
		}
		
		
		Random rand = new Random();
		
		int offer;
		
		if(price>100){
			return null;
		}
		if (price==0) {
			return null;
		}
		else if(price<=10){
			offer = rand.nextInt(price);
		}
		else if(price%2==numberTransporter(getName())%2){
			offer = rand.nextInt(price);
		}
		else{
			offer = rand.nextInt(100) + price + 1;
		}
		
		if (offer == 0){
			offer+=1;
		}
		
		Job newJob = new Job(getName(), idFactory(), origin, destination, offer); 
		
		addJob(newJob);
	
		return newJob.createJobView();
	}
	
	public int numberTransporter(String name){
		String number = name.substring(name.length()-1);
		return Integer.parseInt(number);
	}

	public void operate(String origin, String destination, String name) throws DoesNotOperateException {
		if (regiaoCentro.contains(origin) && regiaoCentro.contains(destination)){
		}
		else if ((regiaoNorte.contains(origin) || regiaoCentro.contains(origin)) && 
			((regiaoNorte.contains(destination) || regiaoCentro.contains(destination)))){
			if (numberTransporter(name)%2!=0){
				throw new DoesNotOperateException(name, origin, destination);
			}
		}
		else if ((regiaoSul.contains(origin) || regiaoCentro.contains(origin)) && 
			(regiaoSul.contains(destination) || regiaoCentro.contains(destination))){
			if (numberTransporter(name)%2!=1){
				throw new DoesNotOperateException(name, origin, destination);
			}
		}
		else{
			throw new DoesNotOperateException(name, origin, destination);
		}
	}

	@Override
	public JobView decideJob(String id, boolean accept) throws BadJobFault_Exception {
		Random rand = new Random();
		int delay;
		
		final Job j;
		if (id==null){
			BadJobFault fault = new BadJobFault();
			fault.setId(id);
			throw new BadJobFault_Exception("invalid ID", fault);
		}
		
		try{
			j = getJobById(id);
		}catch (InvalidIdentifierException iie){
			BadJobFault fault = new BadJobFault();
			fault.setId(id);
			throw new BadJobFault_Exception("invalid ID", fault);
		}

		if (j.getState()!="PROPOSED"){
			BadJobFault fault = new BadJobFault();
			fault.setId(id);
			throw new BadJobFault_Exception("invalid ID", fault);
		}
		
		if(!accept){
			j.setState("REJECTED");
		}
		
		else{
			delay = rand.nextInt(4000) + 1000;
			
			j.setState("ACCEPTED");
		    
			Timer timer = new Timer(delay, new ActionListener() { 
		        @Override
		        public void actionPerformed(ActionEvent ae) {
		           acceptedToHeading(j);
		        }
		    });
		    timer.start();
		}
		return j.createJobView();
	}

	@Override
	public JobView jobStatus(String id) {
		if (id==null){
			return null;
		}
		try {
			Job j = getJobById(id);
			return j.createJobView();
		}catch (InvalidIdentifierException iie){
			return null;
		}
	}

	@Override
	public List<JobView> listJobs() {
		ArrayList<JobView> jobViews = new ArrayList<JobView>();
		ArrayList<Job> jobs = getJobs();
		for (Job j : jobs) {
			jobViews.add(j.createJobView());
		}
		return jobViews;
	}

	@Override
	public void clearJobs() {
		getJobs().clear();
	}
	
	public Job getJobById(String id) throws InvalidIdentifierException {
		ArrayList<Job> jobs = getJobs();
		
		for (Job j: jobs){
			if (j.getIdentifier().equals(id)) {
				return j;
			}
		}
		throw new InvalidIdentifierException(id);
	}
	
	
	public void acceptedToHeading(Job j){
		int delay;
		Random rand = new Random();
		
		delay = rand.nextInt(4000) + 1000;
		
		j.setState("HEADING");
	    
		Timer timer = new Timer(delay, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	           headingToOngoing(j);
	        }
	    });
	    timer.start();
	}
	
	public void headingToOngoing(Job j){
		int delay;
		Random rand = new Random();
		
		delay = rand.nextInt(4000) + 1000;
		
		j.setState("ONGOING");
	    
		Timer timer = new Timer(delay, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	           ongoingToCompleted(j);
	        }
	    });
	    timer.start();
	}
	
	public void ongoingToCompleted(Job j){
		j.setState("COMPLETED");
	}
	
	
	public String idFactory(){
		String id = getId();
		int i = Integer.parseInt(id);
		i++;
		
		if (i > Integer.parseInt(getBoundId())){
			throw new NoAvailableIdentifierException(getName(), i);
		}
		
		setId(Integer.toString(i));
		return id;
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getUddiURL(){
		return uddiURL;
	}
	
	public ArrayList<Job> getJobs() {
		return this.jobs;
	}
	
	public void addJob(Job job) {
		this.jobs.add(job);
	}
	
	public void removeJob(Job job) {
		this.jobs.remove(job);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBoundId() {
		return boundId;
	}

	public void setBoundId(String boundId) {
		this.boundId = boundId;
	}
	
	


}
