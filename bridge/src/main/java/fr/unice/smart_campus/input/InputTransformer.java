package fr.unice.smart_campus.input;

import fr.unice.smart_campus.sensors.SensorData;

/**
 * Transform an input string into a SensorData.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public interface InputTransformer
{

/**
 * Build an input string from the micro controller in a SensorData.
 * 
 * @param receivedData Data received from the micro controller.
 * @return             A sensor data build from the String. 
 */
public SensorData build(String receivedData);
}
