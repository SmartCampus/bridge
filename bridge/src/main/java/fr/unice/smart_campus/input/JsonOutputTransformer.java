
package fr.unice.smart_campus.input;

import org.json.JSONObject;

import fr.unice.smart_campus.data.SensorData;

/**
 * Transform a string received in a JSON format.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class JsonOutputTransformer
implements InputTransformer
{

/**
 * Build a sensor data from received string.
 * 
 * @param receivedData Micro controller String received.
 * @return             The SensorData built from the String.
 */
public SensorData build(String receivedData)
{
   // Build the JSON Object
   JSONObject jsonObject = new JSONObject(receivedData);
   
   // Build the sensor data from the JSON object.
   SensorData result = new SensorData();
   result.setSensorName(jsonObject.getString("n"));
   result.setSensorValue(jsonObject.getDouble("v"));
   result.setSensorTime(System.currentTimeMillis());
   
   // Return the built result.
   return result;
}
}
