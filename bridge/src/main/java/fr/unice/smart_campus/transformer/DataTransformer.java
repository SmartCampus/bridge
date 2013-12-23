package fr.unice.smart_campus.transformer;

import fr.unice.smart_campus.data.SensorData;

/**
 * Transform an input string into a SensorData.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public interface DataTransformer
{

/**
 * Build an input string from the micro controller in a SensorData.
 * 
 * @param receivedData Data received from the micro controller.
 * @return             A sensor data build from the String. 
 */
public SensorData toSensorData(String receivedData);


/**
 * Build a string from a sensor data.
 * 
 * @param sd Sensor data.
 * @return   String representing the sensor data.
 */
public String toString(SensorData sd);


/**
 * Build a string from a sensor data without the string name.
 * 
 * @param sd Sensor data.
 * @return   String representing the sensor data without its name.
 */
public String toStringWithoutName(SensorData sd);
}
