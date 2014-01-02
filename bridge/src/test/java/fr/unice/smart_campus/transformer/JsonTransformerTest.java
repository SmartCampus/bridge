
package fr.unice.smart_campus.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.json.JSONException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import fr.unice.smart_campus.data.SensorData;

/**
 * Test of the JsonTransformer class.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JsonTransformerTest
{

/** Json transformer */
private JsonTransformer transformer;


/**
 * Tests setup.
 */
@Before
public void testSetup()
{
   transformer = new JsonTransformer();
}


/**
 * Test constructor good execution.
 */
@Test
public void test01_Constructor()
{
   assertNotNull(transformer);
}


/**
 * Test toSensorData() method error, with a non-Json string format.
 */
@Test(expected = JSONException.class)
public void test02_ToSensorData_01()
{
   // The String is not a JSON string, an exception should be throw.
   transformer.toSensorData("test");
}


/**
 * Test toSensorData() method good execution, with a JSON formatted string.
 */
@Test
public void test02_ToSensorData_02()
{
   // Build the sensor data.
   SensorData sd = transformer.toSensorData("{\"n\":\"" + "t1" + "\"," + "\"v\":" + 200 + "," + "\"t\":" + System.currentTimeMillis() + "}");
   
   // Check name.
   assertEquals("t1", sd.getSensorName());
   
   // Check time.
   assertEquals(sd.getTime(), sd.getTime());
}


/**
 * Test toString() method error, with a null object given.
 */
@Test(expected = NullPointerException.class)
public void test03_ToString_01()
{
   transformer.toString(null);
}


/**
 * Test toString() method good execution, with a sensor data.
 */
@Test
public void test03_ToString_02()
{
   SensorData sd = transformer.toSensorData("{\"n\":\"" + "t1" + "\"," + "\"v\":" + 200 + "," + "\"t\":" + System.currentTimeMillis() + "}");
   assertEquals("{\"v\":" + 200 + "," + "\"t\":" + sd.getTime() + "," + "\"n\":\"" + "t1\"" + "}", transformer.toString(sd));
}


/**
 * Test toStringWithoutName() method error, with a null object given.
 */
@Test(expected = NullPointerException.class)
public void test04_ToStringWithoutName_01()
{
   transformer.toStringWithoutName(null);
}


/**
 * Test toStringWithoutName() method good execution, with a sensor data.
 */
@Test
public void test04_ToStringWithoutName_02()
{
   SensorData sd = transformer.toSensorData("{\"n\":\"" + "t1" + "\"," + "\"v\":" + 200 + "," + "\"t\":" + System.currentTimeMillis() + "}");
   assertEquals("{\"v\":" + 200 + "," + "\"t\":" + sd.getTime() + "}", transformer.toStringWithoutName(sd));
}
}
