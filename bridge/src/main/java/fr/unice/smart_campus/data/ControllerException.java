
package fr.unice.smart_campus.data;

/**
 * Arduino exceptions manager.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class ControllerException
extends Exception
{

/**
 * Default constructor.
 * 
 * @param message Exception message.
 */
public ControllerException(String message)
{
   super(message);
}


/**
 * Second constructor.
 * 
 * @param e Received exception.
 */
public ControllerException(Exception e)
{
   super(e);
}
}
