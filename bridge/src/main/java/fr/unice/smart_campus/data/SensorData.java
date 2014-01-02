
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
 * Default constructor.
 * 
 * @param name  Name of the sensor.
 * @param value Value of the sensor.
 * @param time  Time where the value was send.
 */
public SensorData(String name, double value, long time)
{
   sensorName = name;
   sensorValue = value;
   sensorTime = time;
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
   return ((sensorTime == sd.sensorTime) && (sensorValue == sd.getValue()) && (sensorName.equals(sd.getSensorName())));
}

}
