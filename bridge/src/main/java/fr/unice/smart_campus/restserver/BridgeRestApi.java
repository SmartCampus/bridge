
package fr.unice.smart_campus.restserver;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

/**
 * Bridge REST Router class
 * @author Cyril Cecchinel
 *
 */
public class BridgeRestApi
extends Application
{

	/**
	 * Create the inbound root
	 * @return A Restlet router object
	 */
public Restlet createInboundRoot()
{
   // Create router from contexte
   Router router = new Router(getContext());

   // Attach inbound routes to resource classes
   router.attach("/boards", BoardsResource.class);
   router.attach("/config", ConfigResource.class);

   return router;
}
}
