
package fr.unice.smart_campus.restserver;

import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * Class which create a rest server
 * @author Cyril Cecchinel
 *
 */
public class BridgeRestServer
{

/**
 * Constructor to RestServer
 * @param portNumber Port to listen
 * @throws Exception
 */
public BridgeRestServer(int portNumber)
throws Exception
{
   // Create a new Component.
   Component component = new Component();

   component.getServers().add(Protocol.HTTP, portNumber);

   // Attach the application.
   component.getDefaultHost().attach(new BridgeRestApi());

   // Start the component.
   component.start();
}

}
