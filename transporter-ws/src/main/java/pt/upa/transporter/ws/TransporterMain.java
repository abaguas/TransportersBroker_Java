package pt.upa.transporter.ws;

import javax.xml.ws.Endpoint;


public class TransporterMain {

    public static void main(String[] args) {
        // Check arguments
        if (args.length < 1) {
            System.err.println("Argument(s) missing!");
            System.err.printf("Usage: java %s url%n", TransporterMain.class.getName());
            return;
        }

        String uddiURL = args[0];
		String name = args[1];
		String url = args[2];

		Endpoint endpoint = null;
        try {
            endpoint = Endpoint.create(new TransporterPort());

            // publish endpoint
            System.out.printf("Starting %s%n", url);
            endpoint.publish(url);

            // wait
            System.out.println("Awaiting connections");
            System.out.println("Press enter to shutdown");
            System.in.read();

        } catch(Exception e) {
            System.out.printf("Caught exception: %s%n", e);
            e.printStackTrace();

        } finally {
            try {
                if (endpoint != null) {
                    // stop endpoint
                    endpoint.stop();
                    System.out.printf("Stopped %s%n", url);
                }
            } catch(Exception e) {
                System.out.printf("Caught exception when stopping: %s%n", e);
            }
        }

    }

}