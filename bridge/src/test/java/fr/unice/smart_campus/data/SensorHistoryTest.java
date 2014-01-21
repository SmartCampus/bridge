
package fr.unice.smart_campus.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

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
 * @throws IOException         File not found.
 * @throws ControllerException Micro controller error.
 */
@Test
public void test03_AddData_02()
throws IOException, ControllerException
{
   // Build the sensor data and add it to the history.
   SensorData sd = new SensorData("t1", 200, System.currentTimeMillis());
   history.addData(sd);

   // Check if sensor data history file has been created.
   SensorValue[] sensorHistory = history.loadAllHistory("t1");
   assertTrue(sensorHistory.length > 0);
   assertTrue((sensorHistory[sensorHistory.length - 1]).equals(sd));
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
   history.loadAllHistory("unknownSensorName");
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
   // Load the history for a first time.
   SensorValue[] sensorHistory1 = history.loadAllHistory("t1");
   assertNotNull(sensorHistory1);

   // Add sensor data to the history.
   SensorData sd1 = new SensorData("t1", 200, System.currentTimeMillis());
   SensorData sd2 = new SensorData("t1", 210, System.currentTimeMillis());
   history.addData(sd1);
   history.addData(sd2);

   // Load the history from the file.
   SensorValue[] sensorHistory2 = history.loadAllHistory("t1");
   assertEquals(sensorHistory2.length, sensorHistory1.length + 2);
   assertEquals(sensorHistory2[sensorHistory2.length - 2], sd1);
   assertEquals(sensorHistory2[sensorHistory2.length - 1], sd2);
}
}
