
package fr.unice.smart_campus.data;

import fr.unice.smart_campus.controller.phidget.MicroControllerPhidget;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

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
private class MyTimerTask
extends TimerTask
{

/** Sensor descriptor */
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
 * Execute action when timer task is executed.
 */
public synchronized void run()
{
   SensorData sensorData;
   try
   {
      // Create the sensor data.
      sensorData = new SensorData(descriptor.getSensorName(), phidget.getPhidget().getSensorValue(descriptor.getPinNumber()),
      System.currentTimeMillis());
      //System.out.println(sensorData.getSensorValue());

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
private MicroControllerPhidget phidget;

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
 * Start refresh of a sensor data.
 * 
 * @param sd Sensor descriptor.
 */
public void startRefresh(SensorDescriptor sd)
{
   // Create the timer.
   MyTimerTask timer = new MyTimerTask(sd);

   // Put the data in the map.
   sensorRepository.put(sd.getSensorName(), timer);
   sensorTimer.schedule(timer, 0, timer.getSensorDescriptor().getFrequency() * 1000);

}


/**
 * Get the timer repository size.
 * 
 * @return The number of sensor managed by this timer repository.d
 */
public int size()
{
   return sensorRepository.size();
}


/**
 * Remove a sensor from the timer manager. 
 * 
 * @param sname Name of the sensor to remove.
 */
public void remove(String sname)
{
   MyTimerTask t = sensorRepository.remove(sname);

   // Stop the task.
   if (t != null)
      t.cancel();
}


/**
 * Change sensor refresh frequency.
 * 
 * @param sd New sensor descriptor.
 * 
 * @throws IllegalArgumentException Runtime exception.
 */
public void changeTimerFrequency(SensorDescriptor sd)
throws IllegalArgumentException
{
   remove(sd.getSensorName());
   startRefresh(sd);
}


/**
 * Terminate all the timers tasks and clear the timer.
 */
public void close()
{
   clear();
   sensorTimer.cancel();
}


/**
 * Clear the sensor timer map.
 */
public void clear()
{
   // Cancel all the timer task.
   for (MyTimerTask t : sensorRepository.values())
   {
      t.cancel();
   }
   sensorTimer.purge();
   sensorRepository.clear();
}
}
