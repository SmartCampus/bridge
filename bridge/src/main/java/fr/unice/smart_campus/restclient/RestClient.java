package fr.unice.smart_campus.restclient;


import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import fr.unice.smart_campus.data.SensorData;

public class RestClient {

	public  static void sendToCollector(SensorData data, String collectorIp)
	{
		ClientResource client = new ClientResource("http://" + collectorIp + "/collector/value");
		JSONObject dataSensorJson = new JSONObject();
		dataSensorJson.accumulate("n", data.getSensorName());
		dataSensorJson.accumulate("v", data.getSensorValue());
		dataSensorJson.accumulate("t", data.getSensorTime());
		StringRepresentation strRepresentation = new StringRepresentation(dataSensorJson.toString());
		strRepresentation.setMediaType(MediaType.APPLICATION_JSON);
		
		try {
			client.post(strRepresentation).write(System.out);
		} catch (Exception e) {
			System.err.println("Error while sending data to collector");
			e.printStackTrace();
		}
		client.release();
	}
	

}
