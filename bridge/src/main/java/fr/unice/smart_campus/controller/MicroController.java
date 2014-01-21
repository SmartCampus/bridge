
package fr.unice.smart_campus.controller;

import fr.unice.smart_campus.data.ControllerException;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.data.SensorHistory;

/**
 * 
 * 
 * @author Jeannot
 *
 */
public interface MicroController
{

/**
 * Close the connection.
 */
public abstract void close();


/**
 * Add a new sensor to the micro controller.
 * 
 * @param sd Description of the sensor to add.
 * 
 * @throws ControllerException Command error.
 */
public abstract void addSensor(SensorDescriptor sd)
throws ControllerException;


/**
 * Delete a sensor from the micro controller.
 * 
 * @param name Name of the sensor to delete.
 * 
 * @throws ControllerException Command error.
 */
public abstract void deleteSensor(String name)
throws ControllerException;


/**
 * Change the sensor refresh data frequency.
 * 
 * @param name Name of the sensor to change the frequency.
 * @param freq New frequency of the sensor in seconds. 
 * 
 * @throws ControllerException Command error.
 */
public abstract void changeSensorFrequency(String name, int freq)
throws ControllerException;


/**
 * Get all information about a sensor.
 * 
 * @param name Name of the sensor we want the information.
 * 
 * @return The description of the named sensor. 
 * 
 * @throws ControllerException Command error.
 */
public abstract SensorDescriptor getSensorInformation(String name)
throws ControllerException;


/**
 * List all the sensor plugged on the micro controller.
 * 
 * @return Return an array that gives all the sensors plugged on the micro controller.
 * 
 * @throws ControllerException Command error.
 */
public abstract SensorDescriptor[] getAllSensors()
throws ControllerException;


/**
 * Get the board Id
 * 
 * @return Return board Id
 * 
 * @throws ControllerException Controller error.
 */
public abstract String getBoardId()
throws ControllerException;


/**
 * Get the controller sensor data history.
 * 
 * @return The controller sensor history.
 */
public abstract SensorHistory getHistory();


/**
 * Reset the micro controller.
 * 
 * @throws ControllerException Controller error.
 */
public abstract void resetController()
throws ControllerException;


/**
 * Execute a command on the micro controller board.
 * 
 * @param string Command to execute.
 * @return       The command response.
 * 
 * @throws ControllerException Controller error.
 */
public abstract String execCommand(String string)
throws ControllerException;


/**
 * This method gives back the configuration of a micro controller.
 * 
 * @return The configuration of a micro controller.
 */
public abstract MicroControllerConfig getConfiguration();

}