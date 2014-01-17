
package fr.unice.smart_campus.controller.phidget;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;

import com.phidgets.InterfaceKitPhidget;
import com.phidgets.Phidget;
import com.phidgets.PhidgetException;
import com.phidgets.event.AttachEvent;
import com.phidgets.event.AttachListener;
import com.phidgets.event.DetachEvent;
import com.phidgets.event.DetachListener;
import com.phidgets.event.ErrorEvent;
import com.phidgets.event.ErrorListener;
import com.phidgets.event.InputChangeEvent;
import com.phidgets.event.InputChangeListener;
import com.phidgets.event.OutputChangeEvent;
import com.phidgets.event.OutputChangeListener;
import com.phidgets.event.SensorChangeEvent;
import com.phidgets.event.SensorChangeListener;

import fr.unice.smart_campus.controller.MicroController;
import fr.unice.smart_campus.controller.MicroControllerConfig;
import fr.unice.smart_campus.data.ControllerException;
import fr.unice.smart_campus.data.CurrentSensorDataRepository;
import fr.unice.smart_campus.data.SensorData;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.data.SensorHistory;
import fr.unice.smart_campus.transformer.DataTransformer;

/**
 * This class represent a Phidget micro controller.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class MicroControllerPhidget
implements MicroController
{

/** Micro controller configuration */
private MicroControllerConfig configuration;

/** File where the Phidgets data are stored */
private File rootDir;

/** Current sensor data repository */
private CurrentSensorDataRepository repository;

/** Sensor data history */
private SensorHistory history;

/** Phidgets interface kit */
private InterfaceKitPhidget phidgetBoard;

/** Data transformer */
private DataTransformer transformer;

/** Log stream */
private PrintStream logStream = System.out;


/**
 * Default constructor.
 * 
 * @param repos         Current sensor data repository. 
 * @param trans         Data transformer.
 * @param rdir          Root directory.
 * @param phidgetSerial Phidget serial number.
 * 
 * @throws IOException         IO error.
 * @throws ControllerException Micro controller error.
 * @throws PhidgetException    Phidget error.
 */
public MicroControllerPhidget(CurrentSensorDataRepository repos, DataTransformer trans, File rdir, int phidgetSerial)
throws ControllerException, IOException, PhidgetException
{
   repository = repos;
   rootDir = rdir;
   configuration = new MicroControllerConfig(new File(rootDir, "controller.cfg"));
   transformer = trans;
   history = new SensorHistory(new File(rdir, "History"), transformer);
   phidgetBoard = open(phidgetSerial);
}


/**
 * Get the micro controller configuration.
 * 
 * @return The micro controller configuration.
 */
public MicroControllerConfig getConfiguration()
{
   return configuration;
}


/**
 * Set the log stream.
 * The default log stream is System.out .
 * 
 * @param stream The new log stream, null for none.
 */
public void setLogStream(PrintStream stream)
{
   logStream = stream;
}


/**
 * Add a sensor to the Phidget micro controller.
 * 
 * @param sd Sensor descriptor to add to the Phidget.n 
 */
public void addSensor(SensorDescriptor sd)
throws ControllerException
{
   try
   {
      // Add the sensor to the configuration.
      configuration.addSensor(sd);

      // Add sensor value to history.
     /* final SensorDescriptor stock = sd;
      final Timer phidgetTimer =  new Timer();
      phidgetTimer.scheduleAtFixedRate(new TimerTask()
      {
         @Override
         public synchronized void run()
         {
            try
            {
               SensorData data = new SensorData(stock.getSensorName(), phidgetBoard.getSensorValue(stock.getPinNumber()), System.currentTimeMillis());
               data.setSensorTimer(phidgetTimer);
               repository.addData(data);
               history.addData(data);
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
      }, 10, sd.getFrequency());*/
   }
   catch (IOException e)
   {
      throw new ControllerException(e);
   }
}


public void deleteSensor(String name)
throws ControllerException
{
   repository.remove(name);
   try
   {
      configuration.delSensor(name);
   }
   catch (IOException e)
   {
      e.printStackTrace();
   }
}


public void changeSensorFrequency(String name, int freq)
throws ControllerException
{
   // Check if sensor exists.
   SensorDescriptor sd = configuration.getSensorFromName(name);
   if (sd == null)
      throw new ControllerException("The sensor '" + name + "' does not exist.");

   // Change sensor frequency.
   sd.setFrequency(freq);

   // Change frequency in the phidget.
   // phidgetBoard.setDataRate(sd.getPinNumber(), freq);
}


public SensorDescriptor getSensorInformation(String name)
throws ControllerException
{
   // Check if sensor exists
   SensorDescriptor sd = configuration.getSensorFromName(name);
   if (sd == null)
      throw new ControllerException("The sensor '" + name + "' does not exist.");

   return sd;
}

public String getBoardId() throws ControllerException {
	// TODO Implement getBoardId logic for Phidgets + Javadoc
	return null;
}

public SensorDescriptor[] getAllSensors()
throws ControllerException
{
   return configuration.getAllSensors();
}


/**
 * Close the phidget board.
 */
public void close()
{
   try
   {
      phidgetBoard.close();
   }
   catch (PhidgetException e)
   {
      e.printStackTrace();
   }
}


/**
 * Open a Phidget Interface Kit.
 * 
 * @param serialNumber Phidget serial number.
 * 
 * @throws PhidgetException Phidget error.
 */
private InterfaceKitPhidget open(int serialNumber)
throws PhidgetException
{
   System.out.println(Phidget.getLibraryVersion());

   InterfaceKitPhidget ik = new InterfaceKitPhidget();
   ik.addAttachListener(new AttachListener()
   {

      // Action when the Phidget is attached.
      public void attached(AttachEvent ae)
      {
         if (logStream != null)
            logStream.println("attachment: " + ae);
      }
   });

   ik.addDetachListener(new DetachListener()
   {

      // Action when the Phidget is detached.
      public void detached(DetachEvent ae)
      {
         if (logStream != null)
            logStream.println("detachment: " + ae);
      }
   });

   ik.addErrorListener(new ErrorListener()
   {

      // Action when error received.
      public void error(ErrorEvent ee)
      {
         if (logStream != null)
            logStream.println("error: " + ee);
      }
   });

   ik.addInputChangeListener(new InputChangeListener()
   {

      // Action when input state change.
      public void inputChanged(InputChangeEvent oe)
      {
         if (logStream != null)
            logStream.println("Input change: " + oe);
      }
   });

   ik.addOutputChangeListener(new OutputChangeListener()
   {

      // Action when output state change.
      public void outputChanged(OutputChangeEvent oe)
      {
         if (logStream != null)
            logStream.println("Output change: " + oe);
      }
   });

   ik.addSensorChangeListener(new SensorChangeListener()
   {

      // Action when sensor value change.
      public void sensorChanged(SensorChangeEvent se)
      {
         if (logStream != null)
            logStream.println("Sensor change: " + se);

         // Create the sensor data from sensor change event.
         SensorDescriptor descriptor = configuration.getSensorFromPin(se.getIndex());
         if (descriptor != null)
         {
            // Add the current value to the repository and to the history.
            SensorData sd = new SensorData(descriptor.getSensorName(), se.getValue(), System.currentTimeMillis());
            repository.addData(sd);
            try
            {
               history.addData(sd);
            }
            catch (IOException e)
            {
               e.printStackTrace();
            }
         }
         else
         {
            if (logStream != null)
               logStream.println("ERROR: received sensor event for unexisting pin number: " + se.getIndex());
         }

      }
   });

   ik.open(serialNumber);
   ik.waitForAttachment();
   return ik;
}



}
