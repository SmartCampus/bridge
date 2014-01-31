package fr.unice.smart_campus.restclient;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ClientResource;

import fr.unice.smart_campus.data.SensorData;


public class BufferedRestClient {
	
	public final int BUFFER_MAX_SIZE = 3;
	
	private List<JSONObject> buffer;
	private String endpoint;
	private int port;
	ClientResource clientRessource;
	Client client;
	
	public BufferedRestClient(String endpoint, int port)
	{
		this.endpoint = endpoint;
		this.port = port;
		this.buffer = new ArrayList<JSONObject>();
		this.clientRessource= new ClientResource("http://" + endpoint + ":" + port + "/collector/value");
		this.client = new Client(new Context(), Protocol.HTTP);
	}
	
	public void fill(SensorData sd)
	{
		JSONObject sdJson = new JSONObject();
		sdJson.accumulate("n", sd.getSensorName());
		sdJson.accumulate("v", sd.getSensorValue());
		sdJson.accumulate("t", sd.getSensorTime());
		buffer.add(sdJson);
		if (buffer.size() == BUFFER_MAX_SIZE)
		{
			sendDatas();
		}
	}

	private void sendDatas() {
		JSONObject dataSensorJson = new JSONObject();
		JSONArray dataArrayJson = new JSONArray(buffer);
		dataSensorJson.accumulate("values", dataArrayJson);
		
		System.out.println("Send data: " + "http://" + endpoint + ":" + port + "/collector/value");
		
		clientRessource.setNext(client);
		StringRepresentation strRepresentation = new StringRepresentation(dataSensorJson.toString());
		strRepresentation.setMediaType(MediaType.APPLICATION_JSON);
		
		try {
			clientRessource.post(strRepresentation);
		} catch (Exception e) {
			System.err.println("Error while sending data to collector");
			e.printStackTrace();
		}
		clientRessource.release();
		buffer.clear();
		
	}

	public String getEndpoint() {
		return endpoint;
	}

	public int getPort() {
		return port;
	}
	
	public int getWaitingQueueSize()
	{
		return this.buffer.size();
	}
	
}
