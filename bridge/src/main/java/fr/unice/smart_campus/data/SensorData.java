
package fr.unice.smart_campus.data;

import fr.unice.smart_campus.Utils;

/**
 * Model the sensor data and informations, like name, time and sensor value.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class SensorData
extends SensorValue
{

/** Sensor name */
private String sensorName;


/**
 * Default constructor.
 * 
 * @param name  Name of the sensor.
 * @param value Value of the sensor.
 * @param time  Time where the value was send.
 */
public SensorData(String name, double value, long time)
{
   super(value, time);
   sensorName = name;
}


/**
 * Get the sensor name.
 * 
 * @return The name of the sensor.
 */
public String getSensorName()
{
   return sensorName;
}


/**
 * Set the sensor name.
 * 
 * @param name New sensor name.
 */
public void setSensorName(String name)
{
   sensorName = name;
}


/**
 * Test equality between two Sensor Data.
 * 
 * @param obj Object to compare with this.
 * @return   true if equals, false if not.
 */
public boolean equals(Object obj)
{
   if (!(obj instanceof SensorData))
      return false;
   
   SensorData sd = (SensorData) obj;
   return ((super.equals(obj)) && (Utils.equals(sensorName, sd.sensorName))); 
}
}
