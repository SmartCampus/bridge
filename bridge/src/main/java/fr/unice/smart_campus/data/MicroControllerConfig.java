
package fr.unice.smart_campus.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import fr.unice.smart_campus.controller.MicroController;

/**
 * Represents the Arduino config. This class is responsible for : 
 * - generate commands to add or remove sensors to the Arduino, 
 * - store locally an image of the Arduino configuration, 
 * - store the configuration in a persistent repository (file system)
 * to be able to rebuild the Arduino config after a restart.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class MicroControllerConfig
{

/** The database of sensors */
private LastSensorDataRepository repository;

/** Micro controller currently in use */
private MicroController microController;

/** Map of sensors descriptors */
private HashMap<String, SensorDescriptor> sensorsDescriptions;

/** Config root directory */
private File rootDirectory;


/**
 * Default constructor.
 * 
 * @param controller Controller currently in use.
 * @param dir        Directory where is stored the micro controller configuration.
 * @param configMode Launch the Arduino from its previous configuration.
 * 
 * @throws IOException          IO Error.
 * @throws InterruptedException Thread interrupted.
 */
public MicroControllerConfig(MicroController controller, File dir, boolean configMode)
throws IOException, InterruptedException
{
   // Build variables.
   repository = new LastSensorDataRepository();
   sensorsDescriptions = new HashMap<String, SensorDescriptor>();
   microController = controller;

   // Build the directory path.
   rootDirectory = dir;
   if (!(rootDirectory.mkdirs()))
   {
      if (!rootDirectory.exists())
         throw new IOException("Unable to create the history directory : " + rootDirectory.getCanonicalPath());
   }

   // Load Arduino config.
   if (configMode)
      loadConfig();
}


/**
 * Add a new sensor to the sensors repository.
 * 
 * @param sensor String command received.
 * 
 * @throws InterruptedException Thread interrupted.
 * @throws IOException          IO error.
 */
public void addSensor(String sensor)
throws IOException, InterruptedException
{
   // Build the sensor descriptor.
   StringTokenizer tkz = new StringTokenizer(sensor, " ");

   // Get sensor descriptor field values.
   String sensorName = tkz.nextToken();
   int pinNumber = Integer.parseInt(tkz.nextToken());
   int frequency = Integer.parseInt(tkz.nextToken());

   // Build the new SensorDescriptor.
   SensorDescriptor sd = new SensorDescriptor(frequency, pinNumber, sensorName);
   try
   {
      microController.addSensor(sd);
   }
   catch (ControllerException e)
   {
      e.printStackTrace();
   }

   sensorsDescriptions.put(sensorName, sd);
   writeToFile();
}


/**
 * Delete a sensor from the sensors repository.
 * 
 * @param name Name of the sensor to delete.
 * 
 * @throws ControllerException The name is not in the map.
 * @throws InterruptedException Thread interrupted.
 * @throws IOException          IO error.
 */
public void delSensor(String name)
throws ControllerException, IOException, InterruptedException
{
   // Execute the del command. 
   microController.deleteSensor(name);

   // Remove sensor from configuration.
   sensorsDescriptions.remove(name);

   // Remove last sensor data from repository.
   repository.remove(name);
   writeToFile();
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
   File f = new File(rootDirectory, "config");
   if (!(f.exists()))
      f.createNewFile();

   // Append commands at the end of the file.
   PrintWriter pw;
   pw = new PrintWriter(new FileWriter(f));
   try
   {
      for (SensorDescriptor sd : sensorsDescriptions.values())
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
 * @throws InterruptedException Thread interrupted.
 */
private void loadConfig()
throws IOException, InterruptedException
{
   // Check if file exists.
   File configFile = new File(rootDirectory, "config");
   if (!configFile.exists())
      System.out.println("The file : " + configFile.getCanonicalPath() + " does not exist.");

   // Read the config file.
   Scanner scanner = new Scanner(configFile);

   // Read the file line by line.
   while (scanner.hasNextLine())
   {
      // Get the next file line.
      String line = scanner.nextLine();
      String command = "add " + line;
      microController.execCommand(command);

      // Build the new SensorDescriptor.
      SensorDescriptor sd = new SensorDescriptor(line);
      sensorsDescriptions.put(sd.getSensorName(), sd);
   }
   scanner.close();
}
}
