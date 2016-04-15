package pt.upa.transporter.ws.it;
 
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
 
public class ClientTransporterGetPropertyValues {
	String result = "";
	InputStream inputStream;
 
	public String getPropValues(String uddiURL, String name) throws IOException {
 
		try {
			Properties prop = new Properties();
			String propFileName = "test.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			// get the property value and print it out
			String uddi = prop.getProperty(uddiURL);
			String ws1 = prop.getProperty(name);
 
			result = uddi + " " + ws1;
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}
	
	public String getPropValuesEvenClient() throws IOException {
		 
		try {
			Properties prop = new Properties();
			String propFileName = "test.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			// get the property value and print it out
			String uddiURL = prop.getProperty("uddi.url");
			String ws1 = prop.getProperty("ws.name");
 
			result = uddiURL + " " + ws1;
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}
}