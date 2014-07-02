
package fr.unice.smart_campus.controller;

import fr.unice.smart_campus.data.ControllerException;
import fr.unice.smart_campus.data.SensorData;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.restclient.BufferedRestClient;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Represents the MicroController config. This class is responsible for : 
 * - generate commands to add or remove sensors to the MicroController, 
 * - store locally an image of the configuration, 
 * - store the configuration in a persistent repository (file system),
 * - rebuild the MicroController config after a restart.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class MicroControllerConfig
{

/** Map of sensors descriptors */
private ArrayList<SensorDescriptor> sensorsDescriptions;

/** Hashmap of sensor endpoint*/
private HashMap<String, BufferedRestClient> sensorsEndpoints;

/** Config file */
private File configFile;


/**
 * Default constructor.
 * 
 * @param controller Controller currently in use.
 * @param file       File who contains the micro controller configuration.
 * 
 * @throws IOException         IO Error.
 * @throws ControllerException Micro controller error.
 */
public MicroControllerConfig(File file)
throws ControllerException, IOException
{
   // Build variables.
   sensorsDescriptions = new ArrayList<SensorDescriptor>();
   sensorsEndpoints = new HashMap<String, BufferedRestClient>();
   // Build the directory path.
   configFile = file;
   configFile.getParentFile().mkdirs();

   loadConfig();
}


/**
 * Get a sensor in the configuration from its name.
 * 
 * @param sensorName Name of the sensor.
 * @return           The sensor descriptor linked to the name, null if none.
 */
public SensorDescriptor getSensorFromName(String sensorName)
{
   for (SensorDescriptor sd : sensorsDescriptions)
   {
      if (sensorName.equals(sd.getSensorName()))
         return sd;
   }

   return null;
}


/**
 * Get a sensor in the configuration from its pin number.
 * 
 * @param pinNumber The sensor pin number.
 * @return          The sensor descriptor linked to the pin number, null if none.
 */
public SensorDescriptor getSensorFromPin(int pinNumber)
{
   for (SensorDescriptor sd : sensorsDescriptions)
   {
      if (pinNumber == sd.getPinNumber())
         return sd;
   }

   return null;
}


/**
 * Get the configuration file.
 * 
 * @return The configuration file.
 */
public File getConfigFile()
{
   return configFile;
}


/**
 * List all the sensor in the configuration.
 * 
 * @return Return an array that gives all the sensors in the configuration.
 */
public SensorDescriptor[] getAllSensors()
{
   // Build the result.
   SensorDescriptor[] res = new SensorDescriptor[sensorsDescriptions.size()];
   sensorsDescriptions.toArray(res);
   return res;
}


/**
 * Add a new sensor to configuration repository.
 * 
 * @param sensor Sensor to add.
 * 
 * @throws IOException IO error.
 * @throws ControllerException 
 */
public void addSensor(SensorDescriptor sensor)
throws IOException, ControllerException
{
   if (!(sensorsDescriptions.contains(sensor)))
   {
      sensorsDescriptions.add(sensor);
      //writeToFile();
      return;
   }

   throw new ControllerException("The sensor : " + sensor.getSensorName() + " is already in configuration.");
}


/**
 * Delete a sensor from the configuration repository.
 * 
 * @param name Name of the sensor to delete.
 * 
 * @throws IOException IO error.
 */
public void delSensor(String name)
throws IOException
{
   // Remove sensor from configuration.
   for (int i = sensorsDescriptions.size() - 1; i >= 0; i--)
   {
      SensorDescriptor sd = sensorsDescriptions.get(i);
      if (sd.getSensorName().equals(name))
      {
         sensorsDescriptions.remove(i);
         unmapSensor(name);
         break;
      }
   }

   //writeToFile();
}

/**
 * Map a sensor with an endpoint
 * @param name	Sensor name
 * @param endpointUrl End point url
 * @param endpointPort End point Port
 * @throws ControllerException
 */
public void mapSensor(String name, String endpointUrl, int endpointPort) throws ControllerException
{
	if(getSensorFromName(name) != null)
	{
		//TODO: Check cas de la double config
		this.sensorsEndpoints.put(name, new BufferedRestClient(endpointUrl, endpointPort));
		System.out.println("Sensor " + name + " mapped on: " + endpointUrl + ":" + endpointPort);
	}
	else throw new ControllerException("The sensor " + name + " has not been found");
}

public BufferedRestClient getMapping(String sname)
{
	return sensorsEndpoints.get(sname);
}

/**
 * Unmap a sensor
 * @param sensorName Sensor name
 */
public void unmapSensor(String sensorName)
{
	this.sensorsEndpoints.remove(sensorName);
}

/**
 * Clear the configuration.
 */
public void clear()
{
   sensorsDescriptions.clear();
   sensorsEndpoints.clear();
}


/**
 * Write the config into a file. 
 * 
 * @throws IOException IO error.
 */
private void writeToFile()
throws IOException
{
   // Find the file corresponding to this sensor. 
   if (!(configFile.exists()))
      configFile.createNewFile();

   // Append commands at the end of the file.
   PrintWriter pw;
   pw = new PrintWriter(new FileWriter(configFile));
   try
   {
      for (SensorDescriptor sd : sensorsDescriptions)
         pw.println(sd.getSensorName() + " " + sd.getPinNumber() + " " + sd.getFrequency());
   }
   finally
   {
      pw.close();
   }
}


/**
 * Load the micro controller configuration from a file.
 * 
 * @throws IOException          IO error.
 * @throws ControllerException 
 */
private void loadConfig()
throws IOException, ControllerException
{
   // Check if file exists.
   if (!configFile.exists())
   {
      configFile.createNewFile();
      return;
   }

   // Read the config file.
   Scanner scanner = new Scanner(configFile);

   // Read the file line by line.
   while (scanner.hasNextLine())
   {
      // Get the next file line.
      String line = scanner.nextLine();

      // Build the new SensorDescriptor.
      SensorDescriptor sd = new SensorDescriptor(line);
      sensorsDescriptions.add(sd);
   }
   scanner.close();
}

/**
 * Send a data to the appropriate collector
 * @param sd A sensor data
 */
public void sendToCollector(SensorData sd) {
	BufferedRestClient mapping = getMapping(sd.getSensorName());
	if (mapping != null)
		mapping.fill(sd);
		
}
}
