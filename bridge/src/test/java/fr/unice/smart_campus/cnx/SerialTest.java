
package fr.unice.smart_campus.cnx;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Serial connection class tests.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SerialTest
{

/**
 * Test if the constructor throw an exception when a fake port is given.
 * 
 * @throws Exception No valid port is given.
 */
@Test(expected = Exception.class)
public void test01_ConstructorException()
throws Exception
{
   SerialConnection connection = new SerialConnection("test");
   connection.close();
}


/**
 * Test the connection construction.
 * 
 * @throws Exception No valid port is given.
 */
@Test
public void test02_Constructor()
throws Exception
{
   SerialConnection connection = new SerialConnection("COM4");
   assertEquals("COM4", connection.getPortName());
   connection.close();
}


/**
 * Test the sending message method.
 * 
 * @throws Exception Message send error.
 */
@Test
public void test03_SendMessage()
throws Exception
{
   // Build the connection.
   SerialConnection connection = new SerialConnection("COM4");

   // Send a message.
   connection.sendMessage("Hello, world!");
   connection.close();
}


/**
 * Test the sending message after close.
 * 
 * @throws Exception Send message error.
 */
@Test(expected = Exception.class)
public void test04_SendMessageAfterClose()
throws Exception
{
   SerialConnection connection = new SerialConnection("COM4");
   connection.close();
   connection.sendMessage("Hello, world!");
}
}
