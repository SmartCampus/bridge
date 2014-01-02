
package fr.unice.smart_campus.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import fr.unice.smart_campus.cnx.ControllerConnection;
import fr.unice.smart_campus.cnx.SerialConnection;
import fr.unice.smart_campus.data.ControllerException;
import fr.unice.smart_campus.data.CurrentSensorDataRepository;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.transformer.DataTransformer;
import fr.unice.smart_campus.transformer.JsonTransformer;

/**
 * Test of MicroControllerConfig class.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D.
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MicroControllerConfigTest
{

/** Micro controller */
private MicroController microController;


/**
 * Tests setup.
 * 
 * @throws Exception 
 */
@Before
public void testSetup()
throws Exception
{
   // Build the test attributes.
   createController();
   microController.resetController();
}


/**
 * Close the connection when a test is finish.
 */
@After
public void testEnd()
{
   microController.close();
}


/**
 * Test addSensor() method good execution.
 * 
 * @throws ControllerException Micro controller error.
 * @throws IOException         IO error.
 */
@Test
public void test01_AddSensor()
throws IOException, ControllerException
{
   microController.addSensor(new SensorDescriptor("t1", 2, 3));

   // Check if file exists.
   File f = new File("ControllerDatas/controller.cfg");
   assertTrue(f.exists());

   // Check file first line.
   Scanner scanner = new Scanner(f);
   assertTrue(scanner.hasNext());

   // Build the sensor descriptor from the read line.
   SensorDescriptor sd = new SensorDescriptor(scanner.nextLine());
   assertEquals("t1", sd.getSensorName());
   assertEquals(2, sd.getPinNumber());
   assertEquals(3, sd.getFrequency());

   // Close the scanner.
   scanner.close();
}


/**
 * Test the delSensor() method error with an unknown sensor name.
 * 
 * @throws IOException         IO error.
 * @throws ControllerException Micro controller error.
 */
@Test(expected = ControllerException.class)
public void test02_DelSensor()
throws ControllerException, IOException
{
   microController.deleteSensor("test");
}


/**
 * @throws Exception 
 * 
 * 
 */
@Test
public void test03_TestAll()
throws Exception
{
   microController.addSensor(new SensorDescriptor("t1", 2, 3));
   microController.addSensor(new SensorDescriptor("t2", 3, 4));

   // Reset the connection.
   microController.close();
   createController();
   SensorDescriptor[] sd = microController.getAllSensors();
   assertEquals(2, sd.length);
   assertEquals("t1", sd[0].getSensorName());
   assertEquals("t2", sd[1].getSensorName());
   
   // Delete a sensor.
   microController.deleteSensor("t1");
   microController.close();
   createController();
   sd = microController.getAllSensors();
   assertEquals(1, sd.length);
   assertEquals("t2", sd[0].getSensorName());
}


/**
 * Create controller.
 * 
 * @throws Exception 
 */
private void createController()
throws Exception
{
   // Build the test attributes.
   ControllerConnection connection = (SerialConnection) new SerialConnection("COM4");
   DataTransformer transformer = new JsonTransformer();
   CurrentSensorDataRepository repository = new CurrentSensorDataRepository();
   File rootDir = new File("ControllerDatas");
   microController = new MicroController(connection, transformer, repository, rootDir);
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
      // Delete the files in the directory.
      String[] files = file.list();
      if (files != null)
      {
         for (String temp : files)
         {
            File fileDelete = new File(file, temp);
            deleteFile(fileDelete);
         }
      }
   }

   // Delete the file.
   file.delete();
}
}
