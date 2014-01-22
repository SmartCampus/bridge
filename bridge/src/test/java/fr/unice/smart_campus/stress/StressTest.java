
package fr.unice.smart_campus.stress;

import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import fr.unice.smart_campus.Constants;
import fr.unice.smart_campus.cnx.ControllerConnection;
import fr.unice.smart_campus.cnx.SerialConnection;
import fr.unice.smart_campus.controller.arduino.MicroControllerArduino;
import fr.unice.smart_campus.data.CurrentSensorDataRepository;
import fr.unice.smart_campus.data.SensorDescriptor;
import fr.unice.smart_campus.transformer.DataTransformer;
import fr.unice.smart_campus.transformer.JsonTransformer;

/**
 * This class test the solidity of the connection code. 
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StressTest
{

/** Micro controller */
private MicroControllerArduino microController;


/**
 * Test mutliple open connection for crash test.
 * 
 * @throws Exception Connection error.
 */
@Test
public void test01_OpenAndClose()
throws Exception
{
   for (int i = 0; i < 10; i++)
   {
      ControllerConnection connection = new SerialConnection(Constants.PORT_NAME);
      connection.close();
   }
}


/**
 * Test the MicroController memory management by adding and removing 
 * sensor.
 * 
 * @throws Exception General error.
 */
@Test
public void test02_AddAndRemove()
throws Exception
{
   createController();

   // Stress test.
   for (int i = 0; i < 1000; i++)
   {
      System.out.println("Loop : " + i);
      microController.addSensor(new SensorDescriptor("t1", 2, 3, "0.0.0.0", 0));
      microController.deleteSensor("t1");
   }
}


/**
 * Create controller.
 * 
 * @throws Exception 
 */
private void createController()
throws Exception
{
   // Build the test attributes.
   ControllerConnection connection = (SerialConnection) new SerialConnection(Constants.PORT_NAME);
   DataTransformer transformer = new JsonTransformer();
   CurrentSensorDataRepository repository = new CurrentSensorDataRepository();
   File rootDir = new File(Constants.ARDUINO_DATA_PATH);
   microController = new MicroControllerArduino(connection, transformer, repository, rootDir);
   microController.resetController();
}
}
