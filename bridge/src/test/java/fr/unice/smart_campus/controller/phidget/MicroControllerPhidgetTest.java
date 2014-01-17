
package fr.unice.smart_campus.controller.phidget;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.phidgets.PhidgetException;

import fr.unice.smart_campus.Configuration;
import fr.unice.smart_campus.Constants;
import fr.unice.smart_campus.controller.MicroControllerConfig;
import fr.unice.smart_campus.data.ControllerException;
import fr.unice.smart_campus.data.CurrentSensorDataRepository;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.transformer.DataTransformer;
import fr.unice.smart_campus.transformer.JsonTransformer;

/**
 * Test the MicroControllerPhidgetTest.java . 
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MicroControllerPhidgetTest
{

/** Micro controller */
private MicroControllerPhidget controller;

/** Micro controller configuration */
private Configuration phidgetConfig;


/**
 * Set the test parameters.
 * 
 * @throws PhidgetException    Phidget error.
 * @throws IOException         IO error.
 * @throws ControllerException Controller error.
 */
@Before
public void testSetup()
throws ControllerException, IOException, PhidgetException
{
   System.out.println();
   System.out.println("=========== BUILD MICRO CONTROLLER ===========");

   // Build the Phidget configuration.
   phidgetConfig = new Configuration(new File(Constants.PHIDGET_CONFIG_PATH));

   // Build the test attributes.
   int phidgetSerialNumber = phidgetConfig.getPhidgetSerialNumber("controller1");
   DataTransformer transformer = new JsonTransformer();
   CurrentSensorDataRepository repository = new CurrentSensorDataRepository();
   File rootDir = new File(Constants.PHIDGET_DATA_PATH);
   controller = new MicroControllerPhidget(repository, transformer, rootDir, phidgetSerialNumber);
   controller.getConfiguration().clear();
}


/**
 * Close the Phidget board after each tests.
 */
@After
public void testEnd()
{
   controller.close();
}


/**
 * Test the constructor good execution.
 */
@Test
public void test01_Constructor_01()
{
   assertNotNull(controller);
}


/**
 * Test the addSensor() method good execution.
 * 
 * @throws ControllerException Controller error.
 */
@Test
public void test02_AddSensor_01()
throws ControllerException
{
   SensorDescriptor sd = new SensorDescriptor("t1", 2, 3);
   controller.addSensor(sd);
}


/**
 * Test the addSensor() method error.
 * 
 * @throws ControllerException Controller error.
 */
@Test(expected = ControllerException.class)
public void test02_AddSensor_02()
throws ControllerException
{
   SensorDescriptor sd = new SensorDescriptor("t1", 2, -3);
   controller.addSensor(sd);
}


/**
 * Test the delSensor() method good execution.
 * 
 * @throws ControllerException Controller error.
 */
@Test
public void test03_DelSensor_01()
throws ControllerException
{
   // Add the sensor and check if it has been added.
   SensorDescriptor sd = new SensorDescriptor("t1", 2, 3);
   controller.addSensor(sd);
   assertNotNull(controller.getConfiguration().getSensorFromName("t1").equals(sd));
   assertNotNull(controller.getSensorTimerRepository().get("t1"));

   // Delete the sensor.
   controller.deleteSensor("t1");
   assertNull(controller.getConfiguration().getSensorFromName("t1"));
   assertNull(controller.getSensorTimerRepository().get("t1"));
}


/**
 * Test the getAllSensors() method good execution.
 * 
 * @throws ControllerException Controller error.
 */
@Test
public void test04_GetAllSensors()
throws ControllerException
{
   // Add two sensors to the configuration.
   controller.addSensor(new SensorDescriptor("t1", 2, 3));
   controller.addSensor(new SensorDescriptor("t2", 3, 4));

   // Check if the configuration is not null.
   SensorDescriptor[] sensorTab = controller.getAllSensors();
   assertNotNull(sensorTab);
   assertEquals(2, sensorTab.length);
   assertEquals("t1", sensorTab[0].getSensorName());

}


/**
 * Test the getConfiguration() method good execution.
 */
@Test
public void test05_GetConfiguration()
{
   MicroControllerConfig config = controller.getConfiguration();
   assertNotNull(config);
}


/**
 * Test the getSensorInformation() method good execution.
 * 
 * @throws ControllerException Controller error.
 */
@Test
public void test06_GetSensorInformation()
throws ControllerException
{
   // Add a new sensor to the controller.
   controller.addSensor(new SensorDescriptor("t1", 2, 3));

   // Get sensor information.
   SensorDescriptor info = controller.getSensorInformation("t1");
   assertNotNull(info);
   assertEquals("t1", info.getSensorName());
   assertEquals(2, info.getPinNumber());
   assertEquals(3, info.getFrequency());
}


/**
 * Test the resetController() method good execution.
 * 
 * @throws ControllerException Controller error.
 */
@Test
public void test07_ResetController()
throws ControllerException
{
   // Add a new sensor to the controller.
   controller.addSensor(new SensorDescriptor("t1", 2, 3));
   
   // Reset the micro controller board.
   controller.resetController();
   assertEquals(0, controller.getConfiguration().getAllSensors().length);
}
}
