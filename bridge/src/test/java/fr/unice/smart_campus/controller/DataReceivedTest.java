
package fr.unice.smart_campus.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import fr.unice.smart_campus.Configuration;
import fr.unice.smart_campus.Constants;
import fr.unice.smart_campus.data.ControllerException;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.data.SensorValue;

/**
 * Test if a data has been received at the good frequency.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DataReceivedTest
{

/** MicroController board */
private MicroController controller;


/**
 * Test setup.
 * 
 * @throws Exception Test error.
 */
@Before
public void testSetup()
throws Exception
{
   // Create the micro controller board.
   Configuration config = new Configuration(new File(Constants.TEST_CONFIG_PATH));

   // Get all the micro controllers name.
   String[] controllerNames = config.getAllControllerNames();
   controller = config.createMicroController(controllerNames[1]);
   controller.resetController();
}


/**
 * Close the micro controller after each test.
 */
@After
public void endTest()
{
   controller.close();
}


/**
 * Test if the data are received at the good frequency.
 * 
 * @throws ControllerException  Controller error.
 * @throws InterruptedException Thread error. 
 * @throws IOException          IO error.
 */
@Test
public void test01_DataReceived_01()
throws ControllerException, InterruptedException, IOException
{
   int NB_DATA = 20;
   int FREQUENCY = 1;

   // Get the current time.
   long startTime = System.currentTimeMillis();

   // Add a sensor to the controller.
   controller.addSensor(new SensorDescriptor("t1", 2, FREQUENCY, "0.0.0.0", 0));
   Thread.sleep(NB_DATA * FREQUENCY * 1000);

   // Get the sensor value.
   SensorValue[] values = controller.getHistory().loadHistory("t1", startTime, startTime + (NB_DATA * (FREQUENCY * 1000)));
   assertTrue((values.length <= NB_DATA + 1) && (values.length >= NB_DATA - 1));
}


/**
 * Test if the data are received correctly with a frequency changement during the test.
 * 
 * @throws ControllerException  Controller error.
 * @throws IOException          IO error.
 * @throws InterruptedException Thread error.
 */
@Test
public void test01_DataReceived_02()
throws ControllerException, IOException, InterruptedException
{
   int NB_DATA = 20;
   int FREQUENCY = 1;

   // Get the current time.
   long startTime = System.currentTimeMillis();

   // Add a sensor to the controller.
   controller.addSensor(new SensorDescriptor("t1", 2, FREQUENCY, "0.0.0.0", 0));
   Thread.sleep(NB_DATA * FREQUENCY * 1000);

   // Get the sensor value.
   SensorValue[] values = controller.getHistory().loadHistory("t1", startTime, startTime + (NB_DATA * (FREQUENCY * 1000)));
   assertTrue((values.length <= NB_DATA + 1) && (values.length >= NB_DATA - 1));

   // Change the sensor frequency.
   controller.changeSensorFrequency("t1", FREQUENCY + 1);
   Thread.sleep(NB_DATA * FREQUENCY * 1000);

   // Get the sensor value.
   SensorValue[] values2 = controller.getHistory().loadHistory("t1", startTime, startTime + (NB_DATA * (FREQUENCY * 1000)));
   assertTrue((values2.length <= NB_DATA + 1) && (values2.length >= NB_DATA - 1));
}


/**
 * Test the reception of data at the good frequency from various number of sensor.
 * 
 * @throws ControllerException   Controller error.
 * @throws InterruptedException  Thread error.
 * @throws IOException 
 */
@Test
public void test01_DataReceived_03()
throws ControllerException, InterruptedException, IOException
{
   int NB_DATA = 20;
   int FREQUENCY = 1;

   // Get the current time.
   long startTime = System.currentTimeMillis();

   // Add three sensors to the controller.
   controller.addSensor(new SensorDescriptor("t1", 2, FREQUENCY, "0.0.0.0", 0));
   controller.addSensor(new SensorDescriptor("t2", 3, FREQUENCY + 1, "0.0.0.0", 0));
   controller.addSensor(new SensorDescriptor("t3", 4, FREQUENCY + 2, "0.0.0.0", 0));
   Thread.sleep(NB_DATA * FREQUENCY * 1000);

   // Get the sensor values for t1.
   SensorValue[] values1 = controller.getHistory().loadHistory("t1", startTime, startTime + (NB_DATA * (FREQUENCY * 1000)));
   assertTrue((values1.length <= NB_DATA + 1) && (values1.length >= NB_DATA - 1));
   
   // Get the sensor values for t2.
   SensorValue[] values2 = controller.getHistory().loadHistory("t2", startTime, startTime + (NB_DATA * (FREQUENCY * 1000)));
   assertTrue((values2.length <= (NB_DATA / 2) + 1) && (values2.length >= (NB_DATA / 2) - 1));
   
   // Get the sensor values for t3.
   SensorValue[] values3 = controller.getHistory().loadHistory("t3", startTime, startTime + (NB_DATA * (FREQUENCY * 1000)));
   assertTrue((values3.length <= (NB_DATA / 3) + 1) && (values3.length >= (NB_DATA / 3) - 1));
}
}