
package fr.unice.smart_campus.data;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test of the SensorData class.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SensorDataTest
{

/** Sensor data */
private SensorData data;

/**
 * Test setup.
 */
@Before
public void testSetup()
{
   data = new SensorData("t1", 3, System.currentTimeMillis());
}

/**
 * Constructor test.
 */
@Test
public void test01_Constructor()
{
   assertNotNull(data);
}


/**
 * Setters tests.
 */
@Test
public void test02_SetAttributes()
{
   // Set sensor data values.
   data.setSensorName("t1");
   data.setSensorTime(System.currentTimeMillis());
   data.setSensorValue(30.0);
   
   // Test the values.
   assertEquals("t1", data.getSensorName());
   assertEquals(data.getSensorTime(), System.currentTimeMillis());
}
}
