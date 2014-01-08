
package fr.unice.smart_campus.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import fr.unice.smart_campus.cnx.ControllerConnection;
import fr.unice.smart_campus.data.ControllerException;
import fr.unice.smart_campus.data.CurrentSensorDataRepository;
import fr.unice.smart_campus.data.SensorData;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.data.SensorHistory;
import fr.unice.smart_campus.transformer.DataTransformer;

/**
 * This class treat the messages received from the micro controller.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class MicroController
implements ControllerConnection.Listener
{

/** Number of command */
private int commandError = 0;

/** Connection to the micro controller */
private ControllerConnection connection;

/** Received response (used to notify command sender) */
private String receivedResponse = null;

/** Response start */
private String expectedMessageStart;

/** The last sensor data repository */
private CurrentSensorDataRepository sensorRepository;

/** Sensor history */
private SensorHistory history;

/** Micro controller data transformer */
private DataTransformer transformer;

/** Micro controller configuration */
private MicroControllerConfig configuration;


/**
 * Default constructor.
 * 
 * @param cnx        Connection to the micro controller.
 * @param trans      Data transformer.
 * @param repository Last sensor data repository.
 * @param rdir       Micro controller data root directory.
 * 
 * @throws IOException          File not found.
 * @throws InterruptedException 
 * @throws ControllerException 
 */
public MicroController(ControllerConnection cnx, DataTransformer trans, CurrentSensorDataRepository repository, File rdir)
throws InterruptedException, IOException, ControllerException
{
   // Construct the attributes.
   connection = cnx;
   expectedMessageStart = "I: Setup terminated";
   connection.setConnectionListener(this);
   transformer = trans;
   sensorRepository = repository;
   history = new SensorHistory(new File(rdir, "History"), transformer);
   configuration = new MicroControllerConfig(new File(rdir, "controller.cfg"));

   // Wait for micro controller setup termination.
   waitForMessageStartingBy(10000);

   // Recreate all sensor that were store in the configuration.
   SensorDescriptor[] descriptors = configuration.getAllSensors();
   for (SensorDescriptor sd : descriptors)
      addSensorInternal(sd);

}


/**
 * Get MicroController configuration.
 * 
 * @return The MicroController configuration.
 */
public MicroControllerConfig getConfig()
{
   return configuration;
}


/**
 * Get the number of command errors.
 * 
 * @return Number of command errors.
 */
public int getCommandError()
{
   return commandError;
}


/**
 * Request command execution to the micro controller.
 * 
 * @param cmd Command to execute.
 * @return    Command response.
 * 
 * @throws IOException          IO error.
 * @throws InterruptedException Thread interrupted.
 * @throws ControllerException 
 */
public String execCommand(String cmd)
throws ControllerException
{
   // Clear response String.
   receivedResponse = null;

   // Send the command.
   try
   {
      expectedMessageStart = "R:";
      connection.sendMessage(cmd);

      // Wait for response.
      waitForMessageStartingBy(5000);
   }
   catch (Exception e)
   {
      throw new ControllerException(e);
   }

   // Build the response string.
   String response = receivedResponse.substring(2).trim();
   System.out.println("|MicroController.java->execCommand|:Response: " + response);
   System.out.flush();
   if (!(response.startsWith("0")))
      throw new ControllerException(response);

   return response.substring(1).trim();
}


/**
 * Check if a message is received from the micro controller.
 * 
 * @param msg Received message from the micro controller.
 */
public synchronized void messageReceived(String msg)
{
   // Check data type.
   if (msg.startsWith("D:"))
   {
      try
      {
         sensorDataReceived(msg.substring(2));
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   if (msg.startsWith(expectedMessageStart))
   {
      receivedResponse = msg;
      System.out.println("|MicroController.java->messageReceived|: recieved response : " + receivedResponse);
      notify();
   }

   else
   {
      infoReceived(msg);
   }
}


/**
 * Close the connection.
 */
public void close()
{
   connection.close();
}


/**
 * Add a new sensor to the micro controller.
 * 
 * @param sd Description of the sensor to add.
 * 
 * @throws ControllerException Command error.
 */
public void addSensor(SensorDescriptor sd)
throws ControllerException
{
   // Create sensor in the controller.
   addSensorInternal(sd);
   try
   {
      // Save sensor in controller config.
      configuration.addSensor(sd);
   }
   catch (IOException e)
   {
      throw new ControllerException("Controller config save error : " + e);
   }
}


/**
 * Delete a sensor from the micro controller.
 * 
 * @param name Name of the sensor to delete.
 * 
 * @throws ControllerException Command error.
 */
public void deleteSensor(String name)
throws ControllerException
{
   // Execute the del command. 
   execCommand("del " + name);
   System.out.println("Delete : " + name);
   
   // Remove last sensor data from repository.
   sensorRepository.remove(name);

   // Remove from config
   try
   {
      configuration.delSensor(name);
   }
   catch (IOException e)
   {
      throw new ControllerException("Controller config save error : " + e);
   }
}


/**
 * Change the sensor refresh data frequency.
 * 
 * @param name Name of the sensor to change the frequency.
 * @param freq New frequency of the sensor in seconds. 
 * 
 * @throws ControllerException Command error.
 */
public void changeSensorFrequency(String name, int freq)
throws ControllerException
{
   if (freq < 0)
      throw new ControllerException("Frequency has to be > to 0.");
   execCommand("freq " + name + " " + freq);
}


/**
 * Get all information about a sensor.
 * 
 * @param name Name of the sensor we want the information.
 * 
 * @return The description of the named sensor. 
 * 
 * @throws ControllerException Command error.
 */
public SensorDescriptor getSensorInformation(String name)
throws ControllerException
{
   String response = execCommand("sensorinfo " + name);

   // Build the object from the String.
   StringTokenizer tkz = new StringTokenizer(response, ": ");

   // Build sensor name.
   tkz.nextToken();
   String sensorName = tkz.nextToken();

   // Build sensor pinNumber.
   tkz.nextToken();
   int pinNumber = Integer.parseInt(tkz.nextToken());

   // Build sensor frequency.
   tkz.nextToken();
   int sensorFrequency = Integer.parseInt(tkz.nextToken());

   // Return the descriptor.
   return new SensorDescriptor(sensorName, pinNumber, sensorFrequency);
}


/**
 * List all the sensor plugged on the micro controller.
 * 
 * @return Return an array that gives all the sensors plugged on the micro controller.
 * 
 * @throws ControllerException Command error.
 */
public SensorDescriptor[] getAllSensors()
throws ControllerException
{
   // Get list of sensors names.
   String lsensors = execCommand("listsensors");
   // Build array of sensor descriptors.
   ArrayList<SensorDescriptor> sensorArray = new ArrayList<SensorDescriptor>();
   StringTokenizer tkz = new StringTokenizer(lsensors, " ");
   while (tkz.hasMoreTokens())
      sensorArray.add(getSensorInformation(tkz.nextToken()));

   // Build the result.
   SensorDescriptor[] res = new SensorDescriptor[sensorArray.size()];
   sensorArray.toArray(res);
   return res;

}


/**
 * Delete all the sensors plugged on the micro controller.
 * 
 * @throws ControllerException Command error.
 */
public void resetController()
throws ControllerException
{
   execCommand("resetsensors");
   configuration.clear();
   sensorRepository.clear();
}


/**
 * Notify new value for a sensor. 
 * This method is called when new sensor data is sent by the micro controller.
 * 
 * @param sdata Sensor data.
 */
public void receivedSensorData(SensorData sdata)
{

}


/**
 * Method called when a sensor data is received.
 * 
 * @param data Sensor data string.
 * 
 * @throws IOException 
 * @throws SecurityException 
 * @throws NoSuchFieldException 
 */
public void sensorDataReceived(String data)
throws NoSuchFieldException, SecurityException, IOException
{
   // Build the sensor data.
   SensorData sd = transformer.toSensorData(data);
   sd.setSensorTime(System.currentTimeMillis());

   // Add data to the data history.
   history.addData(sd);

   // Add data to the data repository.
   sensorRepository.addData(sd);
}


/**
 * Method called when an information data is received.
 * 
 * @param data Sensor data string.
 */
public void infoReceived(String data)
{

}


/**
 * Add a new sensor to the micro controller.
 * 
 * @param sd Description of the sensor to add.
 * 
 * @throws ControllerException Command error.
 */
private void addSensorInternal(SensorDescriptor sd)
throws ControllerException
{
   // Build the string add command
   String command = "add " + sd.getSensorName() + " " + sd.getPinNumber() + " " + sd.getFrequency();
   execCommand(command);
}


/**
 * Wait for message starting by string contained in variable expectedMessageStart.
 * 
 * @param str     Departure message.
 * @param timeout Wait timeout.
 * 
 * @throws InterruptedException 
 * @throws IOException 
 */
private synchronized void waitForMessageStartingBy(int timeout)
throws InterruptedException, IOException
{
   long endTime = System.currentTimeMillis() + timeout;
   while ((receivedResponse == null) || !(receivedResponse.startsWith(expectedMessageStart)))
   {
      long cTime = System.currentTimeMillis();
      if (cTime >= endTime)
         throw new IOException("Timeout waiting for message starting by : " + expectedMessageStart);

      wait(endTime - cTime);
   }
}

/**
 * Test main.
 * 
 * @param args Program arguments.
 * 
 * @throws Exception 
 */
/*public static void main(String[] args)
throws Exception
{
   SerialConnection sc = new SerialConnection("COM4");
   MicroController mc = new MicroController(sc);

   mc.execCommand("add t1 2 3");
}*/
}
