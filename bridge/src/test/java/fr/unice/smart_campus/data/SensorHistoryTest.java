
package fr.unice.smart_campus.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import fr.unice.smart_campus.transformer.DataTransformer;
import fr.unice.smart_campus.transformer.JsonTransformer;

/**
 * Tests for the the SensorHistory class.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SensorHistoryTest
{

/** Sensor history */
private SensorHistory history;

/** Data transformer */
DataTransformer transformer;


/**
 * Test setup.
 * 
 * @throws IOException File not found.
 */
@Before
public void testSetup()
throws IOException
{
   // Build the sensor history.
   transformer = new JsonTransformer();
   File rdir = new File("ControllerDatas/History");
   history = new SensorHistory(rdir, transformer);
}


/**
 * Method to call after each test.
 * 
 * @throws IOException File error.
 */
@After
public void testEnd()
throws IOException
{
   File f = new File("ControllerDatas/history");
   deleteFile(f);
}


/**
 * Test constructor error, with null values.
 * 
 * @throws IOException File not found.
 */
@Test(expected = NullPointerException.class)
public void test01_Constructor_01()
throws IOException
{
   SensorHistory sh = new SensorHistory(null, null);
   assertNull(sh);
}


/**
 * Test constructor good execution.
 */
@Test
public void test01_Constructor_02()
{
   // Check if not null.
   assertNotNull(history);

   // Check if files has been created.
   File file = new File("ControllerDatas/History");
   assertTrue(file.exists());
}


/**
 * Test addData() method error whith a null SensorData.
 * 
 * @throws IOException          File not found.
 */
@Test(expected = NullPointerException.class)
public void test02_AddData_01()
throws IOException
{
   history.addData(null);
}


/**
 * Test addData() method good execution.
 * 
 * @throws IOException File not found.
 */
@Test
public void test03_AddData_02()
throws IOException
{
   // Build the sensor data and add it to the history.
   SensorData sd = transformer.toSensorData("{\"n\":\"" + "t1" + "\"," + "\"v\":" + 200 + "," + "\"t\":" + System.currentTimeMillis() + "}");
   history.addData(sd);

   // Check if sensor data history file has been created.
   File f = new File("ControllerDatas/History/" + sd.getSensorName());
   assertTrue(f.exists());

   // Check file content.
   Scanner scan = new Scanner(f);
   assertTrue(scan.hasNext());
   String line = scan.nextLine();
   assertEquals("{\"v\":" + 200 + "," + "\"t\":" + sd.getTime() + "}", line);
   scan.close();

   // Delete the file to ensure the test continuity.
   f.delete();
}


/**
 * Test loadHistory() method error, with an unknown sensor name.
 * 
 * @throws IOException         IO error.
 * @throws ControllerException Micro controller error.
 */
@Test(expected = ControllerException.class)
public void test04_LoadHistory_01()
throws ControllerException, IOException
{
   history.loadHistory("t2");
}


/**
 * Test loadHistory() method good execution.
 * 
 * @throws IOException         IO error.
 * @throws ControllerException Micro controller error.
 */
@Test
public void test04_LoadHistory_02()
throws IOException, ControllerException
{
   // Add sensor data to the history.
   SensorData sd1 = transformer.toSensorData("{\"n\":\"" + "t1" + "\"," + "\"v\":" + 200 + "," + "\"t\":" + System.currentTimeMillis() + "}");
   SensorData sd2 = transformer.toSensorData("{\"n\":\"" + "t1" + "\"," + "\"v\":" + 210 + "," + "\"t\":" + System.currentTimeMillis() + "}");
   history.addData(sd1);
   history.addData(sd2);

   // Load the history from the file.
   SensorData[] sensorHistory = history.loadHistory("t1");
   assertNotNull(sensorHistory);
   assertEquals(2, sensorHistory.length);
   assertEquals("t1", sensorHistory[0].getSensorName());
   assertEquals("t1", sensorHistory[1].getSensorName());

}


/**
 * Delete a file.
 * 
 * @param file         File to delete.
 * @throws IOException IO error.
 */
private void deleteFile(File file)
throws IOException
{
   // Check if file is a directory or a file.
   if (file.isDirectory())
   {
      // If empty directory, delete it.
      if (file.list().length == 0)
         file.delete();

      else
      {
         // Delete the files in the directory.
         String[] files = file.list();
         for (String temp : files)
         {
            File fileDelete = new File(file, temp);
            deleteFile(fileDelete);
         }

         // Delete the directory if it is empty.
         if (file.list().length == 0)
            file.delete();
      }

   }
   else
      // Delete the file if its a file.
      file.delete();
}
}
