
package fr.unice.smart_campus.data;



/**
 * Model the sensor data and informations, like name, time and sensor value.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class SensorData
{

/** Sensor value */
private double sensorValue;

/** Time where the info is received */
private long sensorTime;

/** Sensor name */
private String sensorName;


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
 * Get the sensor value.
 * 
 * @return Sensor value.
 */
public double getValue()
{
   return sensorValue;
}


/**
 * Set the sensor value. 
 * 
 * @param value New sensor value.
 */
public void setSensorValue(double value)
{
   sensorValue = value;
}

/**
 * Get the sensor update date.
 * 
 * @return Sensor update date.
 */
public long getTime()
{
   return sensorTime;
}


/**
 * Set the new sensor time.
 * 
 * @param time the new sensor time.
 */
public void setSensorTime(long time)
{
   sensorTime = time;
}

}
