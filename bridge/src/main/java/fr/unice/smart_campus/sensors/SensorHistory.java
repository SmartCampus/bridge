
package fr.unice.smart_campus.sensors;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * This class manage the history of sensor values for all sensors of one Arduino. 
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class SensorHistory
{

/** Root directory of the sensors history */
private File rootDirectory;


/**
 * Default constructor.
 * 
 * @param dir Directory where are stored history files.
 * @throws IOException 
 */
public SensorHistory(File dir)
throws IOException
{
   rootDirectory = dir;
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
throws IOException, NoSuchFieldException, SecurityException
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
      pw.println(sd.getSensorName() + " " + sd.getValue());
      System.out.println("print : " + sd.getSensorName() + " " + sd.getValue());
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
 * @throws IOException      IO error.
 * @throws ControllerException Sensor history data file does not exist.
 */
public SensorData[] loadHistory(String sensorName)
throws ControllerException, IOException
{
   // Create the ArrayList of result.
   ArrayList<SensorData> stockDatas = new ArrayList<SensorData>();

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
      SensorData sd = new SensorData(line);
      stockDatas.add(sd);      
   }
   scanner.close();
   
   // Build the result array.
   return (SensorData[]) stockDatas.toArray();
}
}
