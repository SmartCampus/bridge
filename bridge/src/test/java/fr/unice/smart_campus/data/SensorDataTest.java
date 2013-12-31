
package fr.unice.smart_campus.data;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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
   data = new SensorData();
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
   assertEquals(data.getTime(), System.currentTimeMillis());
}
}
