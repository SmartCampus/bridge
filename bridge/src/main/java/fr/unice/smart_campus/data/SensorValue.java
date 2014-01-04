
package fr.unice.smart_campus.data;

/**
 * Class representing the sensor values.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class SensorValue
{

/** Sensor time */
private long sensorTime;

/** Sensor value */
private double sensorValue;


/**
 * Default constructor.
 * 
 * @param value Value of the sensor at the given time.
 * @param time  Time when the value was send (in milliseconds).
 */
public SensorValue(double value, long time)
{
   sensorTime = time;
   sensorValue = value;
}


/**
 * Get the current sensor value.
 * 
 * @return The current sensor value.
 */
public double getSensorValue()
{
   return sensorValue;
}


/**
 * Set the sensor value.
 * 
 * @param newValue New sensor value.
 */
public void setSensorValue(double newValue)
{
   sensorValue = newValue;
}


/**
 * Get the current time of the sensor value.
 * 
 * @return The current time of the sensor value.
 */
public long getSensorTime()
{
   return sensorTime;
}


/**
 * Set the sensor time.
 * 
 * @param newTime New sensor time.
 */
public void setSensorTime(long newTime)
{
   sensorTime = newTime;
}


/**
 * Test equality between two Sensor Data.
 * 
 * @param obj Object to compare with this.
 * @return   true if equals, false if not.
 */
public boolean equals(Object obj)
{
   if (!(obj instanceof SensorValue))
      return false;
   
   SensorValue sv = (SensorValue) obj;
   return ((sensorTime == sv.sensorTime) && (sensorValue == sv.sensorValue));
}
}
