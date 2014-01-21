
package fr.unice.smart_campus.controller;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
   controller = config.createMicroController(controllerNames[0]);
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
public void DataReceived_01()
throws ControllerException, InterruptedException, IOException
{
   int NB_DATA = 20;
   int FREQUENCY = 1;

   // Get the current time.
   long startTime = System.currentTimeMillis();

   // Add a sensor to the controller.
   controller.addSensor(new SensorDescriptor("t1", 2, FREQUENCY));
   Thread.sleep(NB_DATA * FREQUENCY * 1000);

   // Get the sensor value.
   SensorValue[] values = controller.getHistory().loadHistory("t1", startTime, startTime + (NB_DATA * (FREQUENCY * 1000)));
   assertTrue((values.length <= NB_DATA + 1) && (values.length >= NB_DATA - 1));
}
}
