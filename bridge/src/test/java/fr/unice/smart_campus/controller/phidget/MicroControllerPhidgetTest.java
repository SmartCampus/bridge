
package fr.unice.smart_campus.controller.phidget;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.phidgets.PhidgetException;

import fr.unice.smart_campus.Constants;
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

   // Build the test attributes.
   DataTransformer transformer = new JsonTransformer();
   CurrentSensorDataRepository repository = new CurrentSensorDataRepository();
   File rootDir = new File(Constants.ARDUINO_DATA_PATH);
   controller = new MicroControllerPhidget(repository, transformer, rootDir);
}


/**
 * Close th Phidget after each tests.
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
 * Test the add method.
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
}
