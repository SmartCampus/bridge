package fr.unice.smart_campus.restserver;

import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

public class ConfigResource extends ServerResource {

	@Put
	public String putConfig(){
		return "TODO: put config";
	}
}