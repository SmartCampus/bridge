
package fr.unice.smart_campus.data;

import java.util.StringTokenizer;

/**
 * Sensor description.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class SensorDescriptor
{

/** Sensor refresh frequency */
private int sensorFrequency;

/** Sensor pin number */
private int pinNumber;

/** Sensor name */
private String sensorName;


/**
 * Default constructor.
 * 
 * @param frequency Sensor refresh frequency.
 * @param pin       Sensor pin number.
 * @param name      Sensor name.
 * 
 * @throws ControllerException Controller error.
 */
public SensorDescriptor(String name, int pin, int frequency)
throws ControllerException
{
   if ((pin < 0) || (frequency < 0))
      throw new ControllerException("Pin number or frequency is inferior to 0.");

   sensorFrequency = frequency;
   pinNumber = pin;
   sensorName = name;
}


/**
 * Second constrctor.
 * Construct a sensor with a String content.
 * 
 * @param sdata Sensor data value received from the Arduino.
 */
public SensorDescriptor(String sdata)
{
   // Build the object from the String.
   StringTokenizer tkz = new StringTokenizer(sdata, ": ");

   // Build sensor name.
   sensorName = tkz.nextToken();

   // Build sensor pinNumber.
   pinNumber = Integer.parseInt(tkz.nextToken());

   // Build sensor frequency.
   sensorFrequency = Integer.parseInt(tkz.nextToken());
}


/**
 * Get actual sensor frequency.
 * 
 * @return Sensor frequency.
 */
public int getFrequency()
{
   return sensorFrequency;
}


/**
 * Change the sensor frequency.
 * 
 * @param newFreq The new frequency of the sensor.
 */
public void setFrequency(int newFreq)
{
   sensorFrequency = newFreq;
}


/**
 * Get the pin where the sensor is plugged.
 * 
 * @return The pin where the sensor is plugged. 
 */
public int getPinNumber()
{
   return pinNumber;
}


/**
 * Get the sensor name.
 * 
 * @return The sensor name.
 */
public String getSensorName()
{
   return sensorName;
}
}
