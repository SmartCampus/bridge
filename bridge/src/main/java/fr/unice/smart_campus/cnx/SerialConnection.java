
package fr.unice.smart_campus.cnx;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
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
private static final int TIME_OUT = 8000;

/** Default bits per second for CMD port */
private static final int DATA_RATE = 9600;

/** Input stream */
private InputStream input;

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

    try {
        // Open the serial.
        port = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

        // Set port parameters.
        port.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        // Open the streams.
        input = port.getInputStream();
        output = port.getOutputStream();
        Thread.sleep(1000);

        // Add events on the serial port.
        port.addEventListener(new SerialPortEventListener() {

            public void serialEvent(SerialPortEvent arg0) {
                processReceivedEvent(arg0);
            }
        });
        port.notifyOnDataAvailable(true);
    }
    catch (PortInUseException e){
        System.err.println("Can't open port " + portN + " (already used)");
    }

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
 * @throws InterruptedException 
 */
public void sendMessage(String str)
throws IOException, InterruptedException
{
   System.out.println("Send : " + str);

   // Send the command.
   output.write(str.getBytes());
   output.write('\n');
   output.flush();
}


/**
 * Close the connection.
 */
public void close()
{
   if (port != null)
   {
      port.close();
      port = null;
      input = null;
   }
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
         String inputLine = readLine(input);
         System.out.println("Received line : " + inputLine);

         // Notify listener.
         if ((listener != null) && (inputLine != null))
            listener.messageReceived(inputLine);
      }
      catch (Exception e)
      {
         if (port != null)
            e.printStackTrace();
      }
   }
}


/**
 * Read the line.
 * 
 * @param inputStream Input stream to read.
 * @return            The read line, null if end of stream.
 * 
 * @throws IOException 
 */
private String readLine(InputStream inputStream)
throws IOException
{
   StringBuilder line = new StringBuilder();
   int c;

   while ((c = inputStream.read()) >= 0)
   {
      if (c == '\n')
         break;
      if (c != '\r')
         line.append((char) c);
   }

   if ((c < 0) && (line.length() == 0))
      return null;

   return line.toString();
}
}
