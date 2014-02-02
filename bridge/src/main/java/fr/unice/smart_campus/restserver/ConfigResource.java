
package fr.unice.smart_campus.restserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import fr.unice.smart_campus.Main;
import fr.unice.smart_campus.controller.MicroController;
import fr.unice.smart_campus.data.ControllerException;
import fr.unice.smart_campus.data.SensorDescriptor;

/**
 * Configuration Resource class
 * @author Cyril Cecchinel
 *
 */
public class ConfigResource
extends ServerResource
{


	/**
	 * Put config method
	 * Allows to configure sensor network from Internet
	 * 
	 * @throws ControllerException 
	 */
	@Put
	public void putConfig(String jsonString)
	{
		// Get the JSON configuration object
		JSONArray jsonArray = new JSONArray(new JSONObject(jsonString).get("config_sensors").toString());
		
		// Reset all connected boards
		for (MicroController mc : Main.microControllers)
			try {
				mc.resetController();
			} catch (ControllerException e1) {
				System.err.println("Error while reseting boards");
				e1.printStackTrace();
			}
		
		// For each sensor, do
		for (int i = 0; i < jsonArray.length(); i++)
		{
			// Create a current config JSON object
			JSONObject currentConfig = jsonArray.getJSONObject(i);

			String boardName = currentConfig.getString("board");

			String sensorId = currentConfig.getString("id");
			int sensorPin = Integer.parseInt(currentConfig.getString("pin"));
			int sensorFreq = currentConfig.getInt("freq");

			String sensorEndpointIP = null;
			int sensorEndpointPort = 0;

			try{
				sensorEndpointIP = currentConfig.getString("endpointIP");
				sensorEndpointPort = currentConfig.getInt("endpointPort");
			}
			catch (JSONException exception)
			{
				System.err.println("No IP provided");
			}

			// Find the board
			for (MicroController mc : Main.microControllers)
			{
				try {

					if (mc.getBoardId().equals(boardName))
					{
						// Check if the sensor has been already declared
						if (mc.getConfiguration().getSensorFromName(sensorId) != null)
						{
							mc.deleteSensor(sensorId);
						}

						// Add the newly sensor
						mc.addSensor(new SensorDescriptor(sensorId, sensorPin, sensorFreq));

						// Map sensor with its own endpoint
						if (sensorEndpointIP != null && sensorEndpointPort > 0)
							mc.mapSensor(sensorId, sensorEndpointIP, sensorEndpointPort);
					}
				} catch (ControllerException e) {
					System.err.println("ControllerException in ConfigResource");
					e.printStackTrace();
				}
			}
		}
	}
}
