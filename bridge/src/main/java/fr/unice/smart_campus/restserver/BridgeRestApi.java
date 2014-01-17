package fr.unice.smart_campus.restserver;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;



public class BridgeRestApi extends Application {

	public Restlet createInboundRoot(){
		Router router = new Router(getContext());
		
		router.attach("/boards", BoardsResource.class);
		router.attach("/config", ConfigResource.class);
		
		return router;
	}
}
