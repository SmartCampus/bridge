
package fr.unice.smart_campus.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import fr.unice.smart_campus.data.ControllerException;
import fr.unice.smart_campus.data.SensorDescriptor;

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
class MicroControllerConfig
{

/** Map of sensors descriptors */
private ArrayList<SensorDescriptor> sensorsDescriptions;

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
MicroControllerConfig(File file)
throws ControllerException, IOException
{
   // Build variables.
   sensorsDescriptions = new ArrayList<SensorDescriptor>();

   // Build the directory path.
   configFile = file;
   configFile.getParentFile().mkdirs();
   

   loadConfig();
}


/**
 * Add a new sensor to configuration repository.
 * 
 * @param sensor Sensor to add.
 * 
 * @throws IOException IO error.
 */
void addSensor(SensorDescriptor sensor)
throws IOException
{
   sensorsDescriptions.add(sensor);
   writeToFile();
}


/**
 * Delete a sensor from the configuration repository.
 * 
 * @param name Name of the sensor to delete.
 * 
 * @throws IOException IO error.
 */
void delSensor(String name)
throws IOException
{
   // Remove sensor from configuration.
   for (int i = sensorsDescriptions.size() - 1; i >= 0 ;i--)
   {
      SensorDescriptor sd = sensorsDescriptions.get(i);
      if (sd.getSensorName().equals(name))
      {
         sensorsDescriptions.remove(i);
         break;
      }
   }
   
   writeToFile();
}


/**
 * List all the sensor in the configuration.
 * 
 * @return Return an array that gives all the sensors in the configuration.
 */
SensorDescriptor[] getAllSensors()
{
   // Build the result.
   SensorDescriptor[] res = new SensorDescriptor[sensorsDescriptions.size()];
   sensorsDescriptions.toArray(res);
   return res;
}


/**
 * Clear the configuration.
 */
void clear()
{
   sensorsDescriptions.clear();
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
}
