
package fr.unice.smart_campus.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Test of the CurrentSensorDataRepository class.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CurrentSensorDataRepositoryTest
{

/** Current sensor data repository */
private CurrentSensorDataRepository repository;


/**
 * Test setup.
 */
@Before
public void testSetup()
{
   repository = new CurrentSensorDataRepository();
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
 * Test the addData() method good execution.
 */
@Test
public void test02_AddData()
{
   // Build the sensor data value. 
   SensorData sd = new SensorData("t1", 3, System.currentTimeMillis());
   repository.addData(sd);

   // Check if the sensor data has been put in the repository.
   assertEquals(sd, repository.get("t1"));
   assertEquals("t1", repository.get("t1").getSensorName());
}


/**
 * Test the get() method error, when an unknown name is given.
 */
@Test
public void test03_Get_01()
{
   SensorData sd = repository.get("test");
   assertNull(sd);
}


/**
 * Test the get() method good execution. 
 */
@Test
public void test03_Get_02()
{
   // Build the sensor data value. 
   SensorData sd = new SensorData("t1", 3, System.currentTimeMillis());
   repository.addData(sd);

   // Get the sensor data.
   SensorData result = repository.get("t1");
   assertNotNull(result);
   assertEquals(result, sd);
}


/**
 * Test the remove() method good execution.
 * 
 * @throws ControllerException Micro controller error.
 */
@Test
public void test04_Remove()
throws ControllerException
{
   // Build the sensor data value. 
   SensorData sd = new SensorData("t1", 3, System.currentTimeMillis());
   repository.addData(sd);

   // Remove the sensor data.
   repository.remove("t1");
   SensorData result = repository.get("t1");
   assertNull(result);
}
}
