
package fr.unice.smart_campus.data;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

/**
 * Test of the SensorDescriptor class.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SensorDescriptorTest
{

/**
 * Frist constructor good execution test.
 * 
 * @throws ControllerException Controller error.
 */
@Test
public void test01_Constructor_01()
throws ControllerException
{
   // Build the sensor descriptor.
   SensorDescriptor sd = new SensorDescriptor("t1", 2, 3);

   // Test the values.
   assertEquals("t1", sd.getSensorName());
   assertEquals(2, sd.getPinNumber());
   assertEquals(3, sd.getFrequency());
}


/**
 * Second constructor good execution test. 
 */
@Test
public void test01_Constructor_02()
{
   // Build the sensor descriptor from a string.
   SensorDescriptor sd = new SensorDescriptor("t1 2 3");

   // Test the values.
   assertEquals("t1", sd.getSensorName());
   assertEquals(2, sd.getPinNumber());
   assertEquals(3, sd.getFrequency());
}
}
