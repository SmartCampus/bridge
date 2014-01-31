
package fr.unice.smart_campus.restserver;

import java.io.IOException;

import org.json.JSONArray;
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
	 * ADD JAVADOC
	 * 
	 * @return ADD RETURN DOC
	 * @throws ControllerException 
	 */
	@Put
	public void putConfig(String jsonString)
	{
		// Get the JSON configuration object
		JSONArray jsonArray = new JSONArray(new JSONObject(jsonString).get("config_sensors").toString());

		// For each sensor, do
		for (int i = 0; i < jsonArray.length(); i++)
		{
			// Create a current config JSON object
			JSONObject currentConfig = jsonArray.getJSONObject(i);

			String boardName = currentConfig.getString("board");

			String sensorId = currentConfig.getString("id");
			int sensorPin = Integer.parseInt(currentConfig.getString("pin"));
			int sensorFreq = currentConfig.getInt("freq");

			String sensorEndpointIP = currentConfig.getString("endpointIP");
			int sensorEndpointPort = currentConfig.getInt("endpointPort");

			// Find the board
			for (int j = 0; j < Main.getMicroControllers().size() ; j++)
			{
				MicroController microcontroller = Main.getMicroControllers().get(j);
				try {
					if (microcontroller.getBoardId().equals(boardName))
					{
						// Check if the sensor has been already declared
						if (microcontroller.getConfiguration().getSensorFromName(sensorId) != null)
						{
							microcontroller.deleteSensor(sensorId);
						}
						
						// Add the newly sensor
						microcontroller.addSensor(new SensorDescriptor(sensorId, sensorPin, sensorFreq));
						
						// Map sensor with its own endpoint
						microcontroller.mapSensor(sensorId, sensorEndpointIP, sensorEndpointPort);
					}
				} catch (ControllerException e) {
					System.err.println("ControllerException in ConfigResource");
					e.printStackTrace();
				}
			}
		}
	}
}
