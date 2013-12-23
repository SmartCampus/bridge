package fr.unice.smart_campus.data;

import java.util.HashMap;

/**
 * Store all the last sensors data.
 * 
 * @author  Jean Oudot - IUT Nice / Sophia Antipolis - S4D
 * @version 1.0.0
 */
public class LastSensorDataRepository
{

/** Map who store all the sensors */
private HashMap<String, SensorData> sensorsStore = new HashMap<String, SensorData>();


/**
 * Put a sensor with its key in the map.
 * 
 * @param key   Sensor name.
 * @param value Sensor data.
 */
public void addData(SensorData value)
{
   sensorsStore.put(value.getSensorName(), value);
}


/**
 * Get a sensor from its key.
 * 
 * @param key Sensor key (sensor name).
 * @return    The sensor associated to the key, null if none.
 */
public SensorData get(String key)
{
   return sensorsStore.get(key);
}


/**
 * Remove a sensor with its key.
 * 
 * @param key Sensor key.
 */
public void remove(String key)
{
   sensorsStore.remove(key);
}
}
