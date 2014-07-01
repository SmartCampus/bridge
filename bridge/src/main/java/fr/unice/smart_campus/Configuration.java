
package fr.unice.smart_campus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.rapplogic.xbee.api.XBeeAddress16;
import fr.unice.smart_campus.cnx.XBeeConnection;
import org.json.JSONObject;

import fr.unice.smart_campus.cnx.ControllerConnection;
import fr.unice.smart_campus.cnx.SerialConnection;
import fr.unice.smart_campus.controller.MicroController;
import fr.unice.smart_campus.controller.arduino.MicroControllerArduino;
import fr.unice.smart_campus.controller.phidget.MicroControllerPhidget;
import fr.unice.smart_campus.data.CurrentSensorDataRepository;
import fr.unice.smart_campus.transformer.DataTransformer;
import fr.unice.smart_campus.transformer.JsonTransformer;

/**
 * Create the configuration of the all program.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D 
 * @version 1.0.0
 */
public class Configuration
{

/** Program configuration (as JSON) */
private JSONObject configContent;


/**
 * Default constructor.
 * 
 * @param config Configuration file location.
 * 
 * @throws FileNotFoundException 
 */
public Configuration(File config)
throws FileNotFoundException
{
   String fileConfig = loadConfigFile(config);

   // Load config file as JSONobject.
   configContent = new JSONObject(fileConfig);
}


/**
 * Get all the controllers name from the JSONObject.
 * 
 * @return The list of all controllers name.
 */
public String[] getAllControllerNames()
{
   JSONObject controllers = configContent.getJSONObject("controllers");
   if (controllers == null)
      return new String[0];
   return JSONObject.getNames(controllers);
}


/**
 * Create micro controller. 
 * <br>This method create the appropriate instance of micro controller according to its configuration.</br>
 * 
 * @param name The name of the micro controller to create.
 * 
 * @return     A micro controller.
 * @throws Exception 
 */
public MicroController createMicroController(String name)
throws Exception
{
   // Retrieve the controller from the configuration.
   JSONObject controller = getController(name);

   // Get the micro controller type.
   String type = controller.getString("type");

   // Build the data root directory.
   File rootDir = new File(getDataStoragePath());

   // Check controller type.
   if (type.equalsIgnoreCase("arduino"))
   {
      // Get the port name.
      String portName = getControllerPortName(name);

      // Get the connection type.
      String connectionType = getControllerConnectionType(name);

      // Get the controller format.
      DataTransformer transformer;
      String format = getControllerDataFormat(name);
      if (format.equals("json"))
         transformer = new JsonTransformer();
      else
         throw new Exception("Data format : " + format + " unknown.");

      // Build the connection.
      ControllerConnection connection;
      if (connectionType.equals("serial"))
         connection = new SerialConnection(portName);
      else if (connectionType.equals("xbee")){
          String address = controller.getString("address");
          int[] ints = new int[address.length()];
          for (int i = 0; i<address.length(); i++)
              ints[i] = Integer.parseInt(String.valueOf(address.charAt(i)));

          connection = new XBeeConnection(portName, new XBeeAddress16(ints));
      }
      else
         throw new Exception("Connection type : " + connectionType + " unknown.");

      return new MicroControllerArduino(connection, transformer, new CurrentSensorDataRepository(), rootDir);
   }

   else if (type.equalsIgnoreCase("phidget"))
   {
      // Get the phidget serial number.
      int serialNumber = getPhidgetSerialNumber(name);
      
      // Get the controller format.
      DataTransformer transformer;
      String format = getControllerDataFormat(name);
      if (format.equals("json"))
         transformer = new JsonTransformer();
      else
         throw new Exception("Data format : " + format + " unknown.");
      
      return new MicroControllerPhidget(new CurrentSensorDataRepository(), transformer, rootDir, serialNumber);
   }
   
   else
      throw new Exception ("Unknown controller type:" + type);
}


/**
 * Get controller connection.
 * 
 * @param controllerName Name of the controller to get the connection type.
 * 
 * @return The type of controller connection.
 */
public String getControllerConnectionType(String controllerName)
{
   // Get the controller.
   JSONObject controller = getController(controllerName);

   // Return the connection type of the controller.
   return controller.getString("connection");
}


/**
 * Get controller port name.
 * 
 * @param controllerName Name of the controller to get the port name.
 * 
 * @return The port number of the controller.
 */
public String getControllerPortName(String controllerName)
{
   // Get the controller.
   JSONObject controller = getController(controllerName);

   // Return the connection type of the controller.
   return controller.getString("port");
}


/**
 * Get the path of the data root directory.
 * 
 * @return The path of the data root directory.
 */
public String getDataStoragePath()
{
   // Get the root directory of data storage.
   JSONObject config = configContent.getJSONObject("repository");
   return config.getString("rootdir");
}


/**
 * Get the controller data format.
 * 
 * @param controllerName Controller name.
 * @return               The controller data format.
 */
public String getControllerDataFormat(String controllerName)
{
   // Get the controller.
   JSONObject controller = getController(controllerName);

   // Return the controller data format.
   return controller.getString("format");
}


/**
 * Get the repositroy data format.
 * 
 * @return Repository data format.
 */
public String getRepositoryDataFormat()
{
   // Return the controller data format.
   return configContent.getJSONObject("repository").getString("format");
}


/**
 * Get micro controller type.
 * 
 * @return The type of the micro controller.
 */

/**
 * Get the Phidget serial number.
 * 
 * @param name Phidget controller name.
 * 
 * @return The Phidget serial number.
 */
public int getPhidgetSerialNumber(String name)
{
   // Get the phidget controller.
   JSONObject controller = getController(name);

   // Return the phidget controller serial number.
   return controller.getInt("serial");
}


/**
 * Get a controller from his name.
 * 
 * @param controllerName Controller to get.
 * @return               The controller.
 */
private JSONObject getController(String controllerName)
{
   // Get the controller.
   JSONObject controllers = configContent.getJSONObject("controllers");
   return controllers.getJSONObject(controllerName);
}


/**
 * Load the configuration file in a string.
 * 
 * @param f File to load.
 * 
 * @return The file in a string.
 * 
 * @throws FileNotFoundException 
 */
private String loadConfigFile(File f)
throws FileNotFoundException
{
   StringBuilder sb = new StringBuilder();

   // Build the return string from the file.
   Scanner scan = new Scanner(f);
   while (scan.hasNext())
   {
      String line = scan.nextLine().trim();
      if (line.startsWith("//"))
         continue;
      sb.append(line).append("\n");
   }
   scan.close();

   return sb.toString();
}

/**
 * Test program.
 * 
 * @param args Program arguments.
 * 
 * @throws FileNotFoundException 
 */
/*public static void main(String[] args)
throws FileNotFoundException
{
   File f = new File("run/config.cfg");
   Configuration conf = new Configuration(f);
   System.out.println(conf.configContent.toString());
   
   // Get the controllers name list.
   System.out.println("Controllers list");
   String[] controllers = conf.getControllersName();
   for (String ctrl : controllers)
      System.out.println(ctrl);
   
   // Get the controller connection.
   String connection = conf.getControllerConnection("controller1");
   System.out.println(connection);
   
   // Get the controller port.
   String port = conf.getControllerPortName("controller1");
   System.out.println(port);
}*/
}
