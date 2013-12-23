
package fr.unice.smart_campus.controller;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import fr.unice.smart_campus.cnx.ControllerConnection;
import fr.unice.smart_campus.data.ControllerException;
import fr.unice.smart_campus.data.LastSensorDataRepository;
import fr.unice.smart_campus.data.SensorData;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.data.SensorHistory;

/**
 * This class treat the messages received from the micro controller.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class MicroController
implements ControllerConnection.Listener
{

/** Connection to the micro controller */
private ControllerConnection connection;

/** Received response (used to notify command sender) */
private String receivedResponse = null;

/** Response start */
private String expectedResponseStart;

/** The last sensor data repository */
private LastSensorDataRepository sensorRepository;

/** Sensor history */
private SensorHistory history;


/**
 * Default constructor.
 * 
 * @param cnx        Connection to the micro controller.
 * @param repository Last sensor data repository.
 * @param rdir       Micro controller data root directory.
 * 
 * @throws IOException 
 * @throws InterruptedException 
 */
public MicroController(ControllerConnection cnx, LastSensorDataRepository repository, File rdir)
throws InterruptedException, IOException
{
   connection = cnx;
   connection.setConnectionListener(this);
   sensorRepository = repository;
   history = new SensorHistory(new File(rdir, "History"));

   // Wait for micro controller setup termination.
   waitForResponseStartingBy("I: Arduino setup", 10000);
}


/**
 * Request command execution to the micro controller.
 * 
 * @param cmd Command to execute.
 * @return    Command response.
 * 
 * @throws IOException          IO error.
 * @throws InterruptedException Thread interrupted.
 */
public String execCommand(String cmd)
throws IOException, InterruptedException
{
   // Clear response String.
   receivedResponse = null;

   // Send the command.
   connection.sendMessage(cmd);

   // Wait for response.
   waitForResponseStartingBy("R:", 5000);
   return receivedResponse.substring(2).trim();
}


/**
 * Wait for a departure message.
 * 
 * @param str     Departure message.
 * @param timeout Wait timeout.
 * 
 * @throws InterruptedException 
 * @throws IOException 
 */
private synchronized void waitForResponseStartingBy(String str, int timeout)
throws InterruptedException, IOException
{
   expectedResponseStart = str;
   wait(timeout);

   // Check response.
   if (!(receivedResponse.startsWith(expectedResponseStart)))
      throw new IOException("Timeout waiting for message starting by : " + str);
}


public synchronized void messageReceived(String msg)
{
   // Check data type.
   if (msg.startsWith("{"))
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

   if (msg.startsWith(expectedResponseStart))
   {
      receivedResponse = msg;
      notify();
   }

   else
   {
      infoReceived(msg);
   }
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
   // Build the string add command
   String command = "add " + sd.getSensorName() + " " + sd.getPinNumber() + " " + sd.getFrequency();
   try
   {
      String resp = execCommand(command);
      if (!(resp.startsWith("1")))
         throw new ControllerException(resp);
   }
   catch (Exception e)
   {
      throw new ControllerException(e);
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
   try
   {
      execCommand("del " + name);
      sensorRepository.remove(name);
   }
   catch (Exception e)
   {
      throw new ControllerException(e);
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
   try
   {
      execCommand("freq " + name + " " + freq);
   }
   catch (Exception e)
   {
      throw new ControllerException(e);
   }
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
   try
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
      return new SensorDescriptor(sensorFrequency, pinNumber, sensorName);
   }
   catch (Exception e)
   {
      throw new ControllerException(e);
   }
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
   return null;
}


/**
 * Delete all the sensors plugged on the micro controller.
 * 
 * @throws ControllerException Command error.
 */
public void resetController()
throws ControllerException
{

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
   SensorData sd = new SensorData(data);

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
