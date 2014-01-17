package fr.unice.smart_campus.restclient;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;
import org.restlet.resource.ClientResource;

public final class DataSender {
	
	public static void sendData(String sensorName, String sensorValue, String timestamp, String endPoint)
	{
		JSONObject jsonObject = new JSONObject();
		
		
		jsonObject.accumulate("n", sensorName);
		jsonObject.accumulate("v", sensorValue);
		jsonObject.accumulate("t", timestamp);
		
		System.out.println(jsonObject.toString());
		try {
			URL url = new URL("http://localhost:8080/");
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
