
package fr.unice.smart_campus.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import fr.unice.smart_campus.controller.phidget.MicroControllerPhidget;
import fr.unice.smart_campus.transformer.DataTransformer;
import fr.unice.smart_campus.transformer.JsonTransformer;

/**
 * Test of the class SensorTimerRepository.
 * 
 * @author  Jean Oudot- IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SensorTimerRepositoryTest
{

/** Sensor timer repository */
private SensorTimerRepository repository;

/** Phidget micro controller */
private MicroControllerPhidget controller;


/**
 * Test setup.
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

   // Program attributes
   Configuration phidgetConfig = new Configuration(new File(Constants.PHIDGET_CONFIG_PATH));

   // Build the test attributes.
   int phidgetSerialNumber = phidgetConfig.getPhidgetSerialNumber("controller1");
   DataTransformer transformer = new JsonTransformer();
   CurrentSensorDataRepository sensorRepository = new CurrentSensorDataRepository();
   File rootDir = new File(Constants.PHIDGET_DATA_PATH);
   controller = new MicroControllerPhidget(sensorRepository, transformer, rootDir, phidgetSerialNumber);
   controller.getConfiguration().clear();

   // Create the timer repository.
   repository = new SensorTimerRepository(controller);
}


/**
 * Close the micro controller after each test.
 */
@After
public void testEnd()
{
   controller.close();
   repository.close();
}


/**
 * Test the constructor good execution.
 */
@Test
public void test01_Constructor()
{
   assertNotNull(repository);
}


/**
 * Test the put() method error.
 * 
 * @throws ControllerException Controller error.
 */
@Test(expected = ControllerException.class)
public void test02_Put_01()
throws ControllerException
{
   // Put null values in the repository.
   repository.startRefresh(null);
}


/**
 * Test the put() method good execution.
 * 
 * @throws ControllerException Controller error.
 */
@Test
public void test02_Put_02()
throws ControllerException
{
   // Put a value in the repository.
   SensorDescriptor sd = new SensorDescriptor("t1", 2, 3, "0.0.0.0", 0);
   repository.startRefresh(sd);
   
   // Check the values.
   assertEquals(1, repository.size());
}
}
