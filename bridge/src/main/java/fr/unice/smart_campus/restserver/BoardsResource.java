package fr.unice.smart_campus.restserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import fr.unice.smart_campus.Main;
import fr.unice.smart_campus.controller.MicroController;
import fr.unice.smart_campus.data.ControllerException;

public class BoardsResource extends ServerResource {

	@Get
	public String getBoards(){
		JSONArray arr = new JSONArray();
		
		for (MicroController mc : Main.getMicroControllers()){
			JSONObject obj;
			try {
				obj = new JSONObject().put("id", mc.getBoardId());
				arr.put(obj);
			} catch (JSONException | ControllerException e) {
				e.printStackTrace();
			}
		}
		return new JSONObject().put("boards", arr).toString();
	}
}