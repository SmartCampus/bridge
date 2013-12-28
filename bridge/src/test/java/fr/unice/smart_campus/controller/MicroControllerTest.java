
package fr.unice.smart_campus.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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

/** Controller connection */
private ControllerConnection connection;

/** Data transformer */
private DataTransformer transformer;

/** Sensor data repository */
private CurrentSensorDataRepository repository = new CurrentSensorDataRepository();

/** Data root dirctory */
private File rootDir;


/**
 * Tests setup.
 * 
 * @throws Exception 
 */
@Before
public void testSetup()
throws Exception
{
   // Build the test attributes.
   connection = (SerialConnection) new SerialConnection("COM4");
   transformer = new JsonTransformer();
   repository = new CurrentSensorDataRepository();
   rootDir = new File("ControllerDatas");
}


/**
 * Close the connection after each test.
 */
@After
public void testEnd()
{
   connection.close();
}


/**
 * Test if the constructor is well builded.
 * 
 * @throws IOException          File not found.
 * @throws InterruptedException Thread interrupted.
 */
@Test
public void test01_Constructor()
throws InterruptedException, IOException
{
   MicroController mc = new MicroController(connection, transformer, repository, rootDir);
   assertNotNull(mc);
}


/**
 * Test if an invalid command is not executed by the micro controller.
 * 
 * @throws IOException          File not found.
 * @throws InterruptedException Thread interrupted.
 */
@Test
public void test02_InvalidCommand()
throws InterruptedException, IOException
{
   MicroController mc = new MicroController(connection, transformer, repository, rootDir);
   String receivedResponse = mc.execCommand("toto");
   assertFalse(receivedResponse.startsWith("0"));
}


/**
 * Test the listsensors command.
 * 
 * @throws IOException          File not found.
 * @throws InterruptedException Thread interrupted.
 */
@Test
public void test03_CommandListSensors()
throws InterruptedException, IOException
{
   MicroController mc = new MicroController(connection, transformer, repository, rootDir);
   String receivedResponse = mc.execCommand("listsensors");
   assertTrue(receivedResponse.startsWith("0"));
}


/**
 * Test the add command error.
 * 
 * @throws IOException          File not found.
 * @throws InterruptedException Thread interrupted.
 * @throws ControllerException  Micro controller error.
 */
@Test
public void test04_AddSensorError()
throws InterruptedException, IOException, ControllerException
{
   MicroController mc = new MicroController(connection, transformer, repository, rootDir);
   mc.addSensor(new SensorDescriptor("t1 2 3"));
   
}

}
