
package fr.unice.smart_campus.cnx;

import java.io.IOException;

/**
 * Give all the input method for the different connexion type. 
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public interface ControllerConnection
{

/**
 * Listener for asynchronous connection event.
 */
interface Listener
{
/**
 * Notify reception of a message.
 * Implementation of connection is supposed to have is own thread who received the connection
 * and call this method when a message is received.
 * 
 * @param msg Message received.
 */
public void messageReceived(String msg);
}

/**
 * Set the connection listener.
 * 
 * @param listener Listener to add to the connection.
 */
public void setConnectionListener(Listener listener);


/**
 * Send message.
 * 
 * @param str Message to send.
 * 
 * @throws IOException          IO error.
 * @throws InterruptedException Thread interrupted.
 */
public void sendMessage(String str)
throws IOException, InterruptedException;


/**
 * Close the connection.
 */
public void close();
}
