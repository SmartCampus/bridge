
package fr.unice.smart_campus.input;

import java.util.StringTokenizer;

import fr.unice.smart_campus.data.SensorData;

/**
 * Transform a string received in a sensor data with a personal protocol.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class MyProtocolInputTransformer
implements InputTransformer
{

public SensorData build(String receivedData)
{
   // Initialize the object to build.
   SensorData sd = new SensorData();
   
   // Build the object from the String.
   StringTokenizer tkz = new StringTokenizer(receivedData, ": ");
   while (tkz.hasMoreElements())
   {      
      // Build sensor name.
      if (tkz.nextToken().equals("n"))
         sd.setSensorName(tkz.nextToken());
      
      // Build sensor value.
      if (tkz.nextToken().equals("v"))
         sd.setSensorValue(Double.parseDouble(tkz.nextToken()));
   }
   sd.setSensorTime(System.currentTimeMillis());
   return sd;
}

}
