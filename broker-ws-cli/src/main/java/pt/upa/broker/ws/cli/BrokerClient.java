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

    public BrokerClient(String uddiURL, String searchName) {
        fe = new FrontEnd(uddiURL, searchName);
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
	public String requestTransport(String origin, String destination, int price) throws InvalidPriceFault_Exception, UnavailableTransportFault_Exception, UnavailableTransportPriceFault_Exception, UnknownLocationFault_Exception {
		return fe.requestTransport(origin, destination, price);
//		try {
//			return fe.requestTransport(origin, destination, price);
//		} catch (InvalidPriceFault_Exception ipfe){
//			int priceRequested = ipfe.getFaultInfo().getPrice();
//			System.out.println("There are no for negative prices: "+ priceRequested);
//		} catch (UnavailableTransportFault_Exception utfe) {
//			String originRequested = utfe.getFaultInfo().getOrigin();
//			String destinationRequested = utfe.getFaultInfo().getDestination();
//			System.out.println("There are no transports available from: "+ originRequested + "to: " + destinationRequested);
//		} catch (UnavailableTransportPriceFault_Exception utpfe) {
//			int bestPrice = utpfe.getFaultInfo().getBestPriceFound();
//			System.out.println("There are no transports available for this price: "+ price);
//			System.out.println("Best offer was: "+ bestPrice);
//		} catch	(UnknownLocationFault_Exception ulfe) {
//			String location = ulfe.getFaultInfo().getLocation();
//			System.out.println("This location is not valid: "+ location);
//		}
//		return "";
	}

	@Override
	public TransportView viewTransport(String id) throws UnknownTransportFault_Exception {
		return fe.viewTransport(id);
//		try {
//			return fe.viewTransport(id);
//		} catch (UnknownTransportFault_Exception utfe) {
//			String identifier = utfe.getFaultInfo().getId();
//			System.out.println("This identifier isnt valid: "+ identifier);
//		}
//		return null;
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
	public void updateTransport(TransportView transport, String endpoint) {
		fe.updateTransport(transport, endpoint);
	}

	@Override
	public void iAmAlive(String iAmAlive) {
		fe.iAmAlive(iAmAlive);
	}

	public FrontEnd getFe() {
		return fe;
	}

}
