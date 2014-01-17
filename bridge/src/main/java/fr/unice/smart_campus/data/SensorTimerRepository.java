
package fr.unice.smart_campus.data;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import fr.unice.smart_campus.controller.phidget.MicroControllerPhidget;

/**
 * This class manages a timer linked to a sensor.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class SensorTimerRepository
{

/**
 * Represents a timer task.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public static class MyTimerTask
extends TimerTask
{

/** Sensor data */
private SensorDescriptor descriptor;


/**
 * Default constructor.
 * 
 * @param sd Sensor descriptor.
 */
public MyTimerTask(SensorDescriptor sd)
{
   descriptor = sd;
}


/**
 * Get the sensor descriptor.
 * 
 * @return The sensor descriptor.
 */
public SensorDescriptor getSensorDescriptor()
{
   return descriptor;
}


/**
 * Execute action when timer task is create.
 */
public synchronized void run()
{
   SensorData sensorData;
   try
   {
      // Create the sensor data.
      sensorData = new SensorData(descriptor.getSensorName(), phidget.getPhidget().getSensorValue(descriptor.getPinNumber()),
      System.currentTimeMillis());
      System.out.println(sensorData.getSensorValue());

      // Put the data in the history and in the configuration.
      phidget.refreshSensorData(sensorData);
   }
   catch (Exception e)
   {
      e.printStackTrace();
   }
}
}

/** Sensor timer */
private Timer sensorTimer;

/** Phidget micro controller */
private static MicroControllerPhidget phidget;

/** Sensor name linked to a timer task */
private HashMap<String, MyTimerTask> sensorRepository = new HashMap<String, MyTimerTask>();


/**
 * Default constructor.
 * 
 * @param phi Phidget board.
 */
public SensorTimerRepository(MicroControllerPhidget phi)
{
   phidget = phi;
   sensorTimer = new Timer();
}


/**
 * Add a timer task to the repository.
 * 
 * @param sname Sensor name to link with the timer task.
 * @param timer Task to link with the given sensor name.
 * 
 * @throws ControllerException Controller error.
 */
public void put(String sname, MyTimerTask timer)
throws ControllerException
{
   if ((sname == null) || (timer == null))
      throw new ControllerException("The name or the timer are null.");
   
   sensorRepository.put(sname, timer);
   sensorTimer.schedule(timer, 0, timer.getSensorDescriptor().getFrequency() * 1000);
}


/**
 * Get a timer task from a sensor name.
 * 
 * @param sname Sensor name.
 * @return      The timer task link to this name.
 */
public MyTimerTask get(String sname)
{
   return sensorRepository.get(sname);
}


/**
 * Get the timer repository hash map.
 * 
 * @return The sensor timer repository hash map.
 */
public HashMap<String, MyTimerTask> getTimerRepository()
{
   return sensorRepository;
}


/**
 * Delete an entry of the map. 
 * 
 * @param sname Name of the sensor to delete.
 */
public void remove(String sname)
{
   // Get the tasks and cancel it.
   sensorRepository.get(sname).cancel();

   // Remove the map entry.
   sensorRepository.remove(sname);
}


/**
 * Deschedule a timer task from the repository and reschedule it with the new frequency.
 * 
 * @param sname   Sensor to change the timer frequency.
 * @param newFreq New frequency.
 */
public void changeTimerFrequency(String sname, int newFreq)
{
   // Stock the old task.
   MyTimerTask t = sensorRepository.get(sname);

   // Remove the old task.
   sensorRepository.remove(sname);

   // Add the new task, with the new frequency.
   t.getSensorDescriptor().setFrequency(newFreq);
   sensorRepository.put(sname, t);
}


/**
 * Terminate all the timers tasks and clear the timer.
 */
public void close()
{
   // Cancel all the timer task.
   for (String n : sensorRepository.keySet())
   {
      MyTimerTask t = sensorRepository.get(n);
      t.cancel();
   }

   sensorTimer.purge();
}


/**
 * Clear the sensor timer map.
 */
public void clear()
{
   sensorRepository.clear();
}
}
