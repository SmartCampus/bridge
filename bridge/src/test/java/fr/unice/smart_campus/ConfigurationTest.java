
package fr.unice.smart_campus;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.json.JSONException;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Test of Configuration class.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigurationTest
{

/** Configuration directory */
private Configuration config;


/**
 * Test setup.
 * 
 * @throws FileNotFoundException File not found error.
 */
@Before
public void testSetup()
throws FileNotFoundException
{
   File configFile = new File("run/config.cfg");
   config = new Configuration(configFile);
}


/**
 * Test constructor error when an unknown file is given.
 * 
 * @throws FileNotFoundException File not found error.
 */
@Test(expected = FileNotFoundException.class)
public void test01_Constructor_01()
throws FileNotFoundException
{
   Configuration configError = new Configuration(new File("toto"));
   assertNull(configError);
}


/**
 * Test constructor good execution.
 */
@Test
public void test01_Constructor_02()
{
   assertNotNull(config);
}


/**
 * Test the getControllersName() method.
 */
@Test
public void test02_GetControllersName()
{
   // Get the controllers name.
   String[] controllersName = config.getAllControllerNames();

   // Check if good execution.   
   assertEquals(1, controllersName.length);
   assertEquals("controller1", controllersName[0]);
}


/**
 * Test the getControllerConnectionType() method error, 
 * when an unknown controller is given.
 */
@Test(expected = JSONException.class) 
public void test03_GetConnectionType_01()
{
   config.getControllerConnectionType("test");
}


/**
 * Test the getControllerConnectionType() method 
 * good execution.
 */
@Test
public void test03_GetConnectionType_02()
{
   String connectionType = config.getControllerConnectionType("controller1");
   assertEquals("serial", connectionType);
}


/**
 * Test the getControllerPortName() method error, 
 * when an unknown controller is given.
 */
@Test(expected = JSONException.class)
public void test04_GetPortName_01()
{
   config.getControllerPortName("test");
}


/**
 * Test the getControllerPortName() method 
 * good execution.
 */
@Test
public void test04_GetPortName_02()
{
   String portName = config.getControllerPortName("controller1");
   assertEquals("COM4", portName);
}


/**
 * Test the getControllerDataFormat() method error, 
 * when an unknown controller name is given.
 */
@Test(expected = JSONException.class)
public void test05_GetControllerDataFormat_01()
{
   config.getControllerDataFormat("test");
}


/**
 * Test the getControllerDataFormat() method good execution.
 */
@Test
public void test05_GetControllerDataFormat_02()
{
   String dataFormat = config.getControllerDataFormat("controller1");
   assertEquals("json", dataFormat);
}


/**
 * Test the getDataStoragePath() method 
 * good execution.
 */
@Test
public void test06_GetDataStoragePath()
{
   String dataStoragePath = config.getDataStoragePath();
   assertEquals("ControllerDatas", dataStoragePath);
}


/**
 * Test the getRepositoryDataFormat() method good execution.
 */
@Test
public void test07_GetRepositoryDataFormat()
{
   String reposDataFormat = config.getRepositoryDataFormat();
   assertEquals("json", reposDataFormat);
}

}
