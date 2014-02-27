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

/**
 * BufferedRestClient
 * A buffered rest client class
 * @author Cyril Cecchinel
 *
 */
public class BufferedRestClient {
	
	// Set buffer size
	public final int BUFFER_MAX_SIZE = 100;
	
	private List<JSONObject> buffer;
	private String endpoint;
	private int port;
	private ClientResource clientRessource;
	private Client client;
	
	/**
	 * Create a buffered rest client
	 * @param endpoint Collector IP
	 * @param port Collector port
	 */
	public BufferedRestClient(String endpoint, int port)
	{
		this.endpoint = endpoint;
		this.port = port;
		this.buffer = new ArrayList<JSONObject>();
		this.clientRessource= new ClientResource("http://" + endpoint + ":" + port + "/collector/value");
		this.client = new Client(new Context(), Protocol.HTTP);
	}
	
	/**
	 * Fill the buffer
	 * @param sd Sensor data
	 */
	public void fill(SensorData sd)
	{
		JSONObject sdJson = new JSONObject();
		sdJson.accumulate("n", sd.getSensorName());
		sdJson.accumulate("v", String.valueOf((int) sd.getSensorValue()));
		sdJson.accumulate("t", String.valueOf(sd.getSensorTime()));
		buffer.add(sdJson);
		if (buffer.size() == BUFFER_MAX_SIZE)
		{
			sendDatas();
		}
	}

	/**
	 * Send data when the buffer is full
	 */
	private void sendDatas() {
		JSONArray dataArrayJson = new JSONArray(buffer);
		
		System.out.println("Send data: " + "http://" + endpoint + ":" + port + "/collector/value");
		clientRessource.setNext(client);
		StringRepresentation strRepresentation = new StringRepresentation(dataArrayJson.toString());
		strRepresentation.setMediaType(MediaType.APPLICATION_JSON);
		
		try {
			clientRessource.post(strRepresentation);
		} catch (Exception e) {
			System.err.println("Error while sending data to collector");
			e.printStackTrace();
		}
		try {
			client.stop();
		} catch (Exception e) {
			System.err.println("Error while stopping collector connection");
			e.printStackTrace();
		}
		
		buffer.clear();
		
	}
	
	/**
	 * Get the endpoint ip/hostname
	 * @return Endpoint ip/hostname
	 */
	public String getEndpoint() {
		return endpoint;
	}

	/**
	 * Get the endpoint port
	 * @return Endpoint port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Get the amount of data waiting to being send
	 * @return Number of data not already sent
	 */
	public int getWaitingQueueSize()
	{
		return this.buffer.size();
	}
	
}
