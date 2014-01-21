
package fr.unice.smart_campus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.unice.smart_campus.controller.MicroController;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.restserver.RestServer;

/**
 * Main launcher of the program. 
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class Main
{

/** List of all the Micro Controller plugged on the board. */
private static ArrayList<MicroController> microControllers = new ArrayList<MicroController>();


/**
 * Program main.
 * 
 * @param args Program arguments.
 * 
 * @throws Exception 
 */
public static void main(String[] args)
throws Exception
{
   // Build the configuration.
   File configFile = new File(args[0]);
   if (configFile == null)
      System.out.println("The file : " + configFile.getCanonicalPath() + " does not exist.");
   Configuration programConfig = new Configuration(configFile);

   // Build the connection.
   for (String n : programConfig.getAllControllerNames())
   {
      microControllers.add(programConfig.createMicroController(n));
   }

   MicroController ctrl =  microControllers.get(0);
   ctrl.resetController();
   ctrl.addSensor(new SensorDescriptor("t1 2 3"));
   ctrl.addSensor(new SensorDescriptor("t2 4 5"));

   new RestServer(9001);
}


/**
 * Get the Arduino micro controllers.
 * 
 * @return The controllers link to the Arduino.
 */
public static List<MicroController> getMicroControllers()
{
   return microControllers;
}
}
