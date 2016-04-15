package pt.upa.transporter.ws.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.*;
import static org.junit.Assert.*;
import mockit.*;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;
import org.junit.Test;

import pt.upa.transporter.ws.BadLocationFault_Exception;
import pt.upa.transporter.ws.BadPriceFault_Exception;
import pt.upa.transporter.ws.JobStateView;
import pt.upa.transporter.ws.JobView;
import pt.upa.transporter.ws.TransporterPortType;
import pt.upa.transporter.ws.TransporterService;

public class RequestJobIT extends AbstractIT {

	@Override
	protected void populate() {
	
	}
	
	
	
	
}
