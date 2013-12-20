
package fr.unice.smart_campus.cnx;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * This class represents a serial connexion between the micro controller
 * and the bridge.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class Serial
implements InputProtocol
{

/** The serial port */
private SerialPort port;

/** The port we are normally going to use */
private String portName;

/** Response start */
private String expectedResponseStart;

/** Received response (used to notify command sender) */
private String receivedResponse = null;

/** Port time out */
private static final int TIME_OUT = 2000;

/** Default bits per second for CMD port */
private static final int DATA_RATE = 9600;

/**
 *  Convert the bytes received by a InputStreamReader into
 *  characters and displayed them. 
 */
private BufferedReader input;

/** The output stream to the port */
private OutputStream output;


/**
 * Initialize the connection between the computer and the 
 * serial port. 
 * 
 * @param portN Serial port name.
 * @throws Exception 
 */
public Serial(String portN)
throws Exception
{
   portName = portN;

   // Get all the availables ports on the computer. 
   CommPortIdentifier portId = null;
   Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();

   // Find an instance of a serial port who is in PORT_NAME.
   while (portEnum.hasMoreElements())
   {
      CommPortIdentifier currentPortId = (CommPortIdentifier) portEnum.nextElement();
      if (currentPortId.getName().equals(portName))
      {
         portId = currentPortId;
         break;
      }
   }
   if (portId == null)
   {
      throw new IOException("Could not find port : " + portName);
   }

   // Open the serial. 
   port = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

   // Set port parameters.
   port.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

   // Open the streams.
   input = new BufferedReader(new InputStreamReader(port.getInputStream()));
   output = port.getOutputStream();

   // Add events on the serial port.
   port.addEventListener(new SerialPortEventListener()
   {

      public void serialEvent(SerialPortEvent arg0)
      {
         processReceivedEvent(arg0);
      }
   });
   port.notifyOnDataAvailable(true);

   // Wait for Arduino setup termination (to be changed).
   waitForResponseStartingBy("I: Micro controller setup", 10000);
}


/**
 * Request command execution to Arduino.
 * 
 * @param cmd Command to execute.
 * @return    Command response.
 * 
 * @throws IOException          IO error.
 * @throws InterruptedException Thread interrupted.
 */
public synchronized String execCommand(String cmd)
throws IOException, InterruptedException
{
   System.out.println("Send : " + cmd);

   // Clear response String.
   receivedResponse = null;

   // Send the command.
   output.write(cmd.getBytes());
   output.flush();
   Thread.sleep(1500);

   // Wait for response.
   waitForResponseStartingBy("R:", 5000);
   return receivedResponse.substring(2).trim();
}


/**
 * Execute all the commands from a file.
 * 
 * @param filePath Command file path.
 * 
 * @throws InterruptedException Thread interrupted.
 * @throws IOException          IO error.
 */
public void execFileCommand(String filePath)
throws IOException, InterruptedException
{
   Scanner scanner = new Scanner(new File(filePath));

   // Read file line by line and execute each lines as commands.
   while (scanner.hasNextLine())
   {
      String line = scanner.nextLine();
      execCommand(line);
   }
   scanner.close();
}


/**
 * Handle input event on the serial port.
 * 
 * @param oEvent Event receives.
 */
private synchronized void processReceivedEvent(SerialPortEvent oEvent)
{
   if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
   {
      try
      {
         // Wait input to be ready (mandatory, exception o/w).
         while (!input.ready())
         {
            Thread.sleep(100);
         }
         String inputLine = input.readLine();
         System.out.println("Received line : " + inputLine);

         // Check data type.
         if (inputLine.startsWith("{"))
         {
            sensorDataReceived(inputLine.substring(2));
         }

         else if (inputLine.startsWith("R:"))
         {

         }

         else
         {
            infoReceived(inputLine);
         }

         if (inputLine.startsWith(expectedResponseStart))
         {
            receivedResponse = inputLine;
            notify();
         }

      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}


/**
 * Wait for a departure message.
 * 
 * @param str     Departure message.
 * @param timeout Wait timeout.
 * 
 * @throws InterruptedException 
 * @throws IOException 
 */
private synchronized void waitForResponseStartingBy(String str, int timeout)
throws InterruptedException, IOException
{
   expectedResponseStart = str;
   wait(timeout);

   // Check response.
   if (!(receivedResponse.startsWith(expectedResponseStart)))
      throw new IOException("Timeout waiting for message starting by : " + str);
}


/**
 * Get the port name currently in use. 
 * 
 * @return The port name currently in use. 
 */
public String getPortName()
{
   return portName;
}


/**
 * Method called when a sensor data is received.
 * 
 * @param data Sensor data string.
 */
public void sensorDataReceived(String data)
{
}


/**
 * Method called when an information data is received.
 * 
 * @param data Sensor data string.
 */
public void infoReceived(String data)
{
}

}
