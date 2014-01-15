
package fr.unice.smart_campus.controller.phidget;

import java.io.File;
import java.io.IOException;

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
private static InterfaceKitPhidget phidgetBoard;

/** Data transformer */
private DataTransformer transformer;


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
public MicroControllerPhidget(CurrentSensorDataRepository repos, DataTransformer trans, File rdir)
throws ControllerException, IOException, PhidgetException
{
   phidgetBoard = open();
   repository = repos;
   rootDir = rdir;
   configuration = new MicroControllerConfig(new File(rootDir, "controller.cfg"));
   history = new SensorHistory(new File(rdir, "History"), transformer);
}


/**
 * Add a sensor to the Phidget micro controller.
 * 
 * @param sd Sensor descriptor to add to the Phidget.n 
 */
public void addSensor(SensorDescriptor sd)
throws ControllerException
{
   // Check the pin number.
   if (sd.getPinNumber() >= 8)
      throw new ControllerException("The pin number : " + sd.getPinNumber() + " should be inferior to 8.");

   try
   {
      // Add the sensor to the configuration.
      configuration.addSensor(sd);
      //phidgetBoard.setDataRate(3, sd.getFrequency());
   }
   catch (IOException e)
   {
      throw new ControllerException(e);
   }
   /*catch (PhidgetException e)
   {
      throw new ControllerException(e);
   }*/
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
      // TODO Auto-generated catch block
      e.printStackTrace();
   }
}


public void changeSensorFrequency(String name, int freq)
throws ControllerException
{
   // TODO Auto-generated method stub

}


public SensorDescriptor getSensorInformation(String name)
throws ControllerException
{
   // TODO Auto-generated method stub
   return null;
}


public SensorDescriptor[] getAllSensors()
throws ControllerException
{
   // TODO Auto-generated method stub
   return null;
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
 * @throws PhidgetException 
 */
private static InterfaceKitPhidget open()
throws PhidgetException
{
   System.out.println(Phidget.getLibraryVersion());

   InterfaceKitPhidget ik = new InterfaceKitPhidget();
   ik.addAttachListener(new AttachListener()
   {

      public void attached(AttachEvent ae)
      {
         System.out.println("attachment : " + ae);
      }
   });
   ik.addDetachListener(new DetachListener()
   {

      public void detached(DetachEvent ae)
      {
         System.out.println("detachment : " + ae);
      }
   });
   ik.addErrorListener(new ErrorListener()
   {

      public void error(ErrorEvent ee)
      {
         System.out.println("error : " + ee);
      }
   });
   ik.addInputChangeListener(new InputChangeListener()
   {

      public void inputChanged(InputChangeEvent oe)
      {
         System.out.println("Input change : " + oe);
      }
   });
   ik.addOutputChangeListener(new OutputChangeListener()
   {

      public void outputChanged(OutputChangeEvent oe)
      {
         System.out.println("Output change : " + oe);
      }
   });
   ik.addSensorChangeListener(new SensorChangeListener()
   {

      public void sensorChanged(SensorChangeEvent se)
      {
         System.out.println("Sensor change : " + se);
      }
   });

   ik.openAny();
   ik.waitForAttachment();
   return ik;
}
}
