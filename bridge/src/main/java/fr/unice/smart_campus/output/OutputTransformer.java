package fr.unice.smart_campus.output;

import fr.unice.smart_campus.sensors.SensorData;

/**
 * Transform a sensor data to a output string format.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public interface OutputTransformer
{

/**
 * Transform a sensor data to an output string.
 * 
 * @param sensorData Sensor data to transform.
 * @return           Formatted string.
 */
public String build(SensorData sensorData);

}
