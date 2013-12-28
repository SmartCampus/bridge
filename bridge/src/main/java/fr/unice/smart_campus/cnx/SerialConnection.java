
package fr.unice.smart_campus.cnx;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

/**
 * This class represents a serial connexion between the micro controller
 * and the bridge.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class SerialConnection
implements ControllerConnection
{

/** The serial port */
private SerialPort port;

/** The port we are normally going to use */
private String portName;

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

/** Connection listener */
private Listener listener = null;


/**
 * Initialize the connection between the computer and the 
 * serial port. 
 * 
 * @param portN Serial port name.
 * @throws Exception 
 */
public SerialConnection(String portN)
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


public void setConnectionListener(Listener listener)
{
   this.listener = listener;
}


/**
 * Send message.
 * 
 * @param str String to send.
 * 
 * @throws IOException IO error.
 */
public synchronized void sendMessage(String str)
throws IOException
{
   System.out.println("Send : " + str);

   // Send the command.
   output.write(str.getBytes());
   output.flush();
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
         
         // Notify listener.
         if (listener != null)
            listener.messageReceived(inputLine);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
}


/**
 * Close the connection.
 */
public synchronized void close()
{
   if (port != null)
   {
      port.close();
      port = null;
   }
}
}
