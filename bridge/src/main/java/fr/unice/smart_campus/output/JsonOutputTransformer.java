
package fr.unice.smart_campus.output;

import fr.unice.smart_campus.sensors.SensorData;

/**
 * Transform a Sensor data into a JSON formatted string.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class JsonOutputTransformer
implements OutputTransformer
{

public String build(SensorData sensorData)
{
   return "{\"n\":\"" + sensorData.getSensorName() + ",\"v\":" + sensorData.getValue() + ",\"t\":" + sensorData.getTime() + "}";
}

}
