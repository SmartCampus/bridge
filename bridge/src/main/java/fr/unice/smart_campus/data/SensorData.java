
package fr.unice.smart_campus.data;

import java.net.UnknownHostException;
import java.util.StringTokenizer;


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
 * Default constructor
 */
public SensorData()
{

}


/**
 * First constrctor.
 * Construct a sensor with a String content.
 * 
 * @param sdata Sensor data value received from the Arduino. 
 * 
 * @throws UnknownHostException Address error.
 */
public SensorData(String sdata)
{         
   // Build the object from the String.
   StringTokenizer tkz = new StringTokenizer(sdata, ": ");
   while (tkz.hasMoreElements())
   {      
      // Build sensor name.
      if (tkz.nextToken().equals("n"))
         sensorName = tkz.nextToken();
      
      // Build sensor value.
      if (tkz.nextToken().equals("v"))
         sensorValue = Float.parseFloat(tkz.nextToken());
   }
   sensorTime = System.currentTimeMillis();
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
