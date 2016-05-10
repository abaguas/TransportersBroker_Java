package pt.upa.broker.ws.cli;

import java.util.List;
import pt.upa.broker.exception.BrokerClientException;
import pt.upa.broker.ws.BrokerPortType;
import pt.upa.broker.ws.InvalidPriceFault_Exception;
import pt.upa.broker.ws.TransportView;
import pt.upa.broker.ws.UnavailableTransportFault_Exception;
import pt.upa.broker.ws.UnavailableTransportPriceFault_Exception;
import pt.upa.broker.ws.UnknownLocationFault_Exception;
import pt.upa.broker.ws.UnknownTransportFault_Exception;

public class BrokerClient implements BrokerPortType{
	
	private FrontEnd fe = null;

    public BrokerClient(String uddiURL, String name) {
        fe = new FrontEnd(uddiURL, name);
    }
    
    /** constructor with provided web service URL */
    public BrokerClient(String endpointURL) throws BrokerClientException {
		fe = new FrontEnd(endpointURL);
	}
	
	
	@Override
	public String ping(String name) {
		return fe.ping(name);
	}

	@Override
	public String requestTransport(String origin, String destination, int price)
			throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception,
			UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		return fe.requestTransport(origin, destination, price);
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		return fe.viewTransport(id);
	}

	@Override
	public List<TransportView> listTransports() {
		return fe.listTransports();
	}

	@Override
	public void clearTransports() {
		fe.clearTransports();
		
	}

	@Override
	public void updateTransport(TransportView transport) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void iAmAlive(String iAmAlive) {
		// TODO Auto-generated method stub
	}

	public FrontEnd getFe() {
		return fe;
	}

}
