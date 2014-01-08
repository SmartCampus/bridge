
package fr.unice.smart_campus.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import fr.unice.smart_campus.transformer.DataTransformer;

/**
 * This class manage the history of sensor values for all sensors of one micro controller. 
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class SensorHistory
{

/** Root directory of the sensors history */
private File rootDirectory;

/** Data transformer */
private DataTransformer transformer;


/**
 * Default constructor.
 * 
 * @param dir   Directory where are stored history files.
 * @param trans Data transformer.
 * 
 * @throws IOException 
 */
public SensorHistory(File dir, DataTransformer trans)
throws IOException
{
   rootDirectory = dir;
   transformer = trans;

   if (!(rootDirectory.mkdirs()))
   {
      if (!rootDirectory.exists())
         throw new IOException("Unable to create the history directory : " + rootDirectory.getCanonicalPath());
   }
}


/**
 * Add a new sensor data to the history.
 * 
 * @param sd Sensor data to add to the history.
 * 
 * @throws IOException IO error.
 * @throws SecurityException 
 * @throws NoSuchFieldException 
 */
public void addData(SensorData sd)
throws IOException
{
   // Find the file corresponding to this sensor. 
   File f = new File(rootDirectory, sd.getSensorName());
   if (!(f.exists()))
      f.createNewFile();

   // Append sensors data at the end of the file.
   PrintWriter pw;
   pw = new PrintWriter(new FileWriter(f, true));
   try
   {
      pw.println(transformer.toStringWithoutName(sd));
   }
   finally
   {
      pw.close();
   }
}


/**
 * Read all datas of a sensor and put them into an array.
 * 
 * @param sensorName The sensor you want the datas.
 * 
 * @return An array of all datas.
 * 
 * @throws IOException         IO error.
 * @throws ControllerException Sensor history data file does not exist.
 */
public SensorValue[] loadHistory(String sensorName)
throws ControllerException, IOException
{
   // Create the ArrayList of result.
   ArrayList<SensorValue> stockDatas = new ArrayList<SensorValue>();

   // Find the file corresponding to the name.
   File f = new File(rootDirectory, sensorName);
   if (!(f.exists()))
      throw new ControllerException("The file " + f.getCanonicalPath() + "does not exist.");

   // Read the file lines by lines. 
   Scanner scanner = new Scanner(f);
   while (scanner.hasNextLine())
   {
      // Build the ArrayList of datas.
      String line = scanner.nextLine();
      SensorValue sv = transformer.toSensorValue(line);
      stockDatas.add(sv);
   }
   scanner.close();

   // Build the result array.
   SensorValue[] res = new SensorValue[stockDatas.size()];
   stockDatas.toArray(res);
   return res;
}


/**
 * Gives all the data between two time value.
 * 
 * @param name    Sensor name. 
 * @param timeMin Time from which we want to get back the data.
 * @param timeMax Time from which we stop to get back the data.
 * @return        An array containing the data between timeMin and timeMax.
 * 
 * @throws IOException         IO error.
 * @throws ControllerException Micro controller error.
 */
public SensorValue[] readData(long timeMin, long timeMax, String name)
throws ControllerException, IOException
{
   if ((timeMin > timeMax) || (timeMax < timeMin))
      throw new ControllerException("Invalid time.");
   
   // Sensor array result.
   ArrayList<SensorValue> result = new ArrayList<SensorValue>();

   // Get all the datas of the given sensor name.
   SensorValue[] allData = loadHistory(name);

   // Build the result ArrayList.
   for (SensorValue sv : allData)
   {
      while ((sv.getSensorTime() >= timeMin) && (sv.getSensorTime() <= timeMax))
         result.add(sv);
   }

   // Build the result array.
   SensorData[] res = new SensorData[result.size()];
   result.toArray(res);
   return res;
}
}
