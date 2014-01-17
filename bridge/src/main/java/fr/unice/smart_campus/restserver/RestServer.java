package fr.unice.smart_campus.restserver;

import java.util.List;

import org.restlet.Component;
import org.restlet.data.Protocol;

public class RestServer {

	
	/**
	 * Private constructor to RestServer
	 * @param portNumber Port to listen
	 * @param protocol Protocol
	 * @throws Exception
	 */
	public RestServer(int portNumber) throws Exception{
		// Create a new Component.
		Component component = new Component();

		component.getServers().add(Protocol.HTTP, portNumber);

		// Attach the application.
		component.getDefaultHost().attach(new BridgeRestApi());

		// Start the component.
		component.start();
	}	
	
}
