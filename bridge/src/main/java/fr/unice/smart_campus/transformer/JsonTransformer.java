
package fr.unice.smart_campus.transformer;

import org.json.JSONObject;

import fr.unice.smart_campus.data.SensorData;

/**
 * Transform miscellanous objects in a JSON format.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class JsonTransformer
implements DataTransformer
{

/**
 * Build a sensor data from a string in JSON format.
 * 
 * @param jStr Micro controller String received.
 * @return     The SensorData built from the String.
 */
public SensorData toSensorData(String jStr)
{
   // Build the JSON Object
   JSONObject jsonObject = new JSONObject(jStr);
   
   // Build the sensor data from the JSON object.
   SensorData result = new SensorData();
   result.setSensorName(jsonObject.getString("n"));
   result.setSensorValue(jsonObject.getDouble("v"));
   if (jsonObject.has("t"))
      result.setSensorTime(jsonObject.getLong("t"));
   
   // Return the built result.
   return result;
}


/**
 * Build a string from a sensor data.
 * 
 * @param sd Sensor data.
 * @return   JSON string representing the sensor data.
 */
public String toString(SensorData sd)
{
   // Build the JSON object.
   JSONObject obj = new JSONObject();
   obj.put("n", sd.getSensorName());
   obj.put("v", sd.getValue());
   obj.put("t", sd.getTime());
   
   // Return the string.
   return obj.toString();
}


/**
 * Build a string from a sensor data without the string name.
 * 
 * @param sd Sensor data.
 * @return   JSON string representing the sensor data without its name.
 */
public String toStringWithoutName(SensorData sd)
{
   // Build the JSON object.
   JSONObject obj = new JSONObject();
   obj.put("v", sd.getValue());
   obj.put("t", sd.getTime());
   
   // Return the string.
   return obj.toString();
}
}
