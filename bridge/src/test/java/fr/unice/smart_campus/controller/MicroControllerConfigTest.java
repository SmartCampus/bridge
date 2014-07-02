
package fr.unice.smart_campus.controller;

import fr.unice.smart_campus.cnx.ControllerConnection;
import fr.unice.smart_campus.cnx.SerialConnection;
import fr.unice.smart_campus.controller.arduino.MicroControllerArduino;
import fr.unice.smart_campus.data.ControllerException;
import fr.unice.smart_campus.data.CurrentSensorDataRepository;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.transformer.DataTransformer;
import fr.unice.smart_campus.transformer.JsonTransformer;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
private MicroControllerArduino microController;


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
   MicroControllerConfig microControllerConfig = microController.getConfiguration();

   // Check if sensor has been correctly added.
   SensorDescriptor[] descriptors = microControllerConfig.getAllSensors();
   assertEquals(1, descriptors.length);
   assertEquals("t1", descriptors[0].getSensorName());
}


/**
 * Test addSensor() method good execution.
 * 
 * @throws ControllerException Micro controller error.
 * @throws IOException         IO error.
 */
@Test
public void test02_FileTest()
throws IOException, ControllerException
{
   // Get the MicroController configuration.
   MicroControllerConfig microControllerConfig = microController.getConfiguration();

   // Volontary remove file.
   microControllerConfig.getConfigFile().delete();

   // Add a sensor to the file.
   microController.addSensor(new SensorDescriptor("t1", 2, 3));

   // Check if config file has been successfully re created.
   File config = microControllerConfig.getConfigFile();
   assertTrue(config.exists());
}


/**
 * Test the delSensor() method error with an unknown sensor name.
 * 
 * @throws IOException         IO error.
 * @throws ControllerException Micro controller error.
 */
@Test(expected = ControllerException.class)
public void test03_DelSensor()
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
public void test04_TestAll()
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
   microController = new MicroControllerArduino(connection, transformer, repository, rootDir);
}
}
