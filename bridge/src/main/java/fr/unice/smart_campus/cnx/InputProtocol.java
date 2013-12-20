
package fr.unice.smart_campus.cnx;

import java.io.IOException;

/**
 * Give all the input method for the different connexion type. 
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public interface InputProtocol
{

/**
 * Request command execution to Arduino.
 * 
 * @param cmd Command to execute.
 * @return    Command response.
 * 
 * @throws IOException          IO error.
 * @throws InterruptedException Thread interrupted.
 */
public String execCommand(String cmd)
throws IOException, InterruptedException;


/**
 * Method called when a sensor data is received.
 * 
 * @param data Sensor data string.
 */
public void sensorDataReceived(String data);


/**
 * Method called when an information data is received.
 * 
 * @param data Sensor data string.
 */
public void infoReceived(String data);
}
