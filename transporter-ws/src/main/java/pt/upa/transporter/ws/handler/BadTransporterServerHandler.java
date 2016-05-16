package pt.upa.transporter.ws.handler;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;



public class BadTransporterServerHandler implements SOAPHandler<SOAPMessageContext> {


	
	public boolean handleMessage(SOAPMessageContext smc) {
		Boolean outbound = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		if (outbound) {
			try {
            	
                // get SOAP envelope
                SOAPMessage msg = smc.getMessage();
                SOAPPart sp = msg.getSOAPPart();
                SOAPEnvelope se = sp.getEnvelope();

                // add header
                SOAPHeader sh = se.getHeader();
                if (sh == null)
                    sh = se.addHeader();
              
 
                SOAPBody sb = se.getBody();

                
                String content = sb.getFirstChild().getTextContent();
                
                
                if(content.contains("PROPOSED")){
                	System.out.println("Intruder: \"I'm getting the soap message body\"...");
                    
	                StringBuilder builder = new StringBuilder(content);
	                System.out.println("Intruder: \"I'm changing the offer price\"...");
	                
	                builder.setCharAt(builder.length()-9,'9');
	                
	                sb.getFirstChild().setTextContent(builder.toString());
	                System.out.println("Body of Transporter Handler: " + content);
	                System.out.println("Body of Intruder Handler: " + builder.toString());
	                
	                System.out.println("Intruder: \"I finished hacking the system\"! Mwahahahah!");
	                
                
                }

        	}
    		catch (SOAPException |TransformerFactoryConfigurationError e) {
				System.out.printf("Failed to get SOAP header because of %s%n", e);
			}

		} else {


		}

		return true;
	}
	
	
	public boolean handleFault(SOAPMessageContext smc) {
		return true;
	}

	public Set<QName> getHeaders() {
		return null;
	}

	public void close(MessageContext messageContext) {
	}


	
}

