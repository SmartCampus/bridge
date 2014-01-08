
package fr.unice.smart_campus.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import fr.unice.smart_campus.Constants;
import fr.unice.smart_campus.cnx.ControllerConnection;
import fr.unice.smart_campus.cnx.SerialConnection;
import fr.unice.smart_campus.data.ControllerException;
import fr.unice.smart_campus.data.CurrentSensorDataRepository;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.transformer.DataTransformer;
import fr.unice.smart_campus.transformer.JsonTransformer;

/**
 * Unit test of the MicroController class.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MicroControllerTest
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
   System.out.println();
   System.out.println("=========== BUILD MICRO CONTROLLER ===========");
   
   // Build the test attributes.
   ControllerConnection connection = (SerialConnection) new SerialConnection(Constants.PORT_NAME);
   DataTransformer transformer = new JsonTransformer();
   CurrentSensorDataRepository repository = new CurrentSensorDataRepository();
   File rootDir = new File(Constants.DATA_PATH);
   microController = new MicroController(connection, transformer, repository, rootDir);
   microController.resetController();
}


/**
 * Close the connection after each test.
 */
@After
public void testEnd()
{
   microController.close();
}


/**
 * Test if the constructor is well builded.
 */
@Test
public void test01_Constructor()
{
   assertNotNull(microController);
}


/**
 * Test if an invalid command is not executed by the micro controller.
 * 
 * @throws ControllerException  Command error.
 */
@Test(expected = ControllerException.class)
public void test02_InvalidCommand()
throws ControllerException
{
   microController.execCommand("toto");
}


/**
 * Test the good exection of the getAllSensors() method. 
 * getAllSensors() is called by all the other tests which test the method in useful cases.
 * 
 * @throws ControllerException  Micro controller error.
 */
@Test
public void test03_GetAllSensors()
throws ControllerException
{
   SensorDescriptor[] sensorArray = microController.getAllSensors();
   assertEquals(0, sensorArray.length);
}


/**
 * Test the listsensors command.
 * 
 * @throws ControllerException Command error.
 */
@Test
public void test04_CommandListSensors()
throws ControllerException
{
   SensorDescriptor[] sensorArray = microController.getAllSensors();
   assertEquals(0, sensorArray.length);
}


/**
 * Test the add command failure when a wrong sensor frequency is given.
 * 
 * @throws ControllerException  Micro controller error.
 */
@Test(expected = ControllerException.class)
public void test05_CommandAdd_01()
throws ControllerException
{
   microController.addSensor(new SensorDescriptor("t1 2 -3")); // Wrong frequency.
}


/**
 * Test the good execution of the add command.
 * 
 * @throws ControllerException  Micro controller error.
 */
@Test
public void test05_CommandAdd_02()
throws ControllerException
{
   // Execute the commands.
   microController.addSensor(new SensorDescriptor("t1 2 3"));
}


/**
 * Test the good execution of the add command.
 * 
 * @throws ControllerException  Micro controller error.
 */
@Test
public void test05_CommandAdd_03()
throws ControllerException
{
   SensorDescriptor[] array = microController.getAllSensors();
   assertEquals(0, array.length);
   
   // Execute the commands.
   microController.addSensor(new SensorDescriptor("t1 2 3"));
   microController.addSensor(new SensorDescriptor("t2 7 5"));

   // Test the good execution.
   SensorDescriptor[] sensorArray = microController.getAllSensors();
   assertEquals(2, sensorArray.length);
   assertEquals("t1", sensorArray[0].getSensorName());
   assertEquals(2, sensorArray[0].getPinNumber());
   assertEquals(3, (sensorArray[0].getFrequency() / 1000));
   assertEquals("t2", sensorArray[1].getSensorName());
   assertEquals(7, sensorArray[1].getPinNumber());
   assertEquals(5, (sensorArray[1].getFrequency() / 1000));
}


/**
 * Test the case when the same sensor is added multiple time to the board.
 * 
 * @throws ControllerException  Micro controller error.
 */
@Test(expected = ControllerException.class)
public void test05_CommandAdd_04()
throws ControllerException
{
   // Execute the commands.
   microController.addSensor(new SensorDescriptor("t1 2 3"));
   microController.addSensor(new SensorDescriptor("t1 3 4"));
}


/**
 * Testing the delete command failure, when an inexisting sensor is given.
 * 
 * @throws ControllerException  Micro controller error.
 */
@Test(expected = ControllerException.class)
public void test06_CommandDel_01()
throws ControllerException
{
   microController.deleteSensor("t1");
}


/**
 * Test the delete command good execution.
 * 
 * @throws ControllerException  Micro controller error.
 */
@Test
public void test06_CommandDel_02()
throws ControllerException
{
   // Add a sensor to the micro controller board.
   microController.addSensor(new SensorDescriptor("t1 2 3"));

   // Check if the sensor have been created.
   SensorDescriptor[] sensorArray = microController.getAllSensors();
   assertEquals(1, sensorArray.length);

   // Delete the sensor and check if it has been deleted.
   microController.deleteSensor("t1");
   sensorArray = microController.getAllSensors();
   assertEquals(0, sensorArray.length);
}


/**
 * Testing the freq command error when an unknown sensor is given.
 * 
 * @throws ControllerException  Micro controller error.
 */
@Test(expected = ControllerException.class)
public void test07_CommandFreq_01()
throws ControllerException
{
   // Change a frequency of an unknown sensor.
   microController.changeSensorFrequency("t1", 2);
}


/**
 * Testing the freq command error when an invalid frequency is given.
 *
 * @throws ControllerException  Micro controller error.
 */
@Test(expected = ControllerException.class)
public void test07_CommandFreq_02()
throws ControllerException
{
   // Add a sensor to the micro controller.
   microController.addSensor(new SensorDescriptor("t1 2 3"));

   // Give an invalid frequency to the sensor.
   microController.changeSensorFrequency("t1", -1);
}


/**
 * Test the good execution of the freq command.
 * 
 * @throws ControllerException  Micro controller error.
 */
@Test
public void test07_CommandFreq_03()
throws ControllerException
{
   // Add a sensor to the micro controller.
   SensorDescriptor sd = new SensorDescriptor("t1 2 3");
   microController.addSensor(sd);

   // Change the sensor frequency.
   microController.changeSensorFrequency("t1", 5);

   // Check if the frequency has been changed.
   sd = microController.getSensorInformation("t1");
   assertEquals(5, (sd.getFrequency() / 1000));
}


/**
 * Testing the sensorinfo command error when an uknown sensor name is given.
 * 
 * @throws ControllerException  Micro controller error.
 */
@Test(expected = ControllerException.class)
public void test08_CommandSensorInfo_01()
throws ControllerException
{
   microController.getSensorInformation("t1");
}


/**
 * Test the good execution of the sensor info command. 
 * 
 * @throws ControllerException  Micro controller error.
 */
@Test
public void test08_CommandSensorInfo_02()
throws ControllerException
{
   // Add a sensor to the micro controller.
   microController.addSensor(new SensorDescriptor("t1 2 3"));

   // Get the information of the sensor added.
   SensorDescriptor sd = microController.getSensorInformation("t1");
   assertEquals("t1", sd.getSensorName());
   assertEquals(2, sd.getPinNumber());
   assertEquals(3, (sd.getFrequency() / 1000));
}


/**
 * Test the good execution of the resetsensor command.
 * 
 * @throws ControllerException Micro controller error.
 */
@Test
public void test09_CommandResetSensors_01()
throws ControllerException
{
   // Add a sensor to the micro controller.
   microController.addSensor(new SensorDescriptor("t1 2 3"));
   
   // Reset the micro controller.
   microController.resetController();
   
   // Check if the reset was effective.
   SensorDescriptor[] sensorArray = microController.getAllSensors();
   assertEquals(0, sensorArray.length);

}

}
