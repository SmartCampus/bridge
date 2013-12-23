
package fr.unice.smart_campus;

import java.io.File;
import java.util.ArrayList;

import fr.unice.smart_campus.cnx.ControllerConnection;
import fr.unice.smart_campus.cnx.SerialConnection;
import fr.unice.smart_campus.controller.MicroController;
import fr.unice.smart_campus.data.CurrentSensorDataRepository;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.transformer.DataTransformer;
import fr.unice.smart_campus.transformer.JsonTransformer;

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
   for (String n : programConfig.getControllersName())
   {
      microControllers.add(createMicroController(n, programConfig));
   }
   
   MicroController ctrl = microControllers.get(0);
   ctrl.addSensor(new SensorDescriptor("t1 2 3"));
}


/**
 * Create a MicroController.
 * 
 * @return The micro controller created.
 * @throws Exception 
 */
private static MicroController createMicroController(String name, Configuration config)
throws Exception
{
   // Build the data root directory.
   File rootDir = new File(config.getDataStoragePath());

   // Get the port name.
   String portName = config.getControllerPortName(name);

   // Get the connection type.
   String connectionType = config.getControllerConnection(name);
   
   // Get the controller format.
   DataTransformer transformer;
   String format = config.getControllerDataFormat(name);
   if (format.equals("json"))
      transformer = new JsonTransformer();
   else
      throw new Exception("Data format : " + format + " unknown.");
      

   // Build the connection.
   ControllerConnection connection;
   if (connectionType.equals("serial"))
      connection = new SerialConnection(portName);
   else
       throw new Exception("Connection type : " + connectionType + " unknown.");
   
   return new MicroController(connection, transformer, new CurrentSensorDataRepository(), rootDir);
}
}