package fr.unice.smart_campus.cnx.xbee;

import com.rapplogic.xbee.api.*;
import com.rapplogic.xbee.api.wpan.RxResponse16;
import com.rapplogic.xbee.util.ByteUtils;
import fr.unice.smart_campus.Utils;
import fr.unice.smart_campus.cnx.ControllerConnection;
import gnu.io.CommPortIdentifier;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

/**
 * This class represent a hardware XBee port
 * Created by Cyril Cecchinel on 09/07/2014.
 */
public class XBeePort implements Observer {

    /** Network configuration file */
    private static final String NETWORK_CONFIG_FILE = "run/network.cfg";

    /** XBee Connection */
    private XBee cnx;

    /** Connection listener */
    private ControllerConnection.Listener listener = null;

    /** XBee port instance (singleton) */
    private static XBeePort instance = null;

    /** Prevent echo reception */
    private String echoPrevention = "";

    /** Default bits per second for CMD port */
    private static final int DATA_RATE = 9600;

    /** Incoming message buffer */
    private XBeeBuffer buffer;

    /**
     * Get the XBeePort instance
     * @return XBeePort instance (singleton)
     * @throws IOException
     */
    public synchronized static XBeePort getInstance() throws IOException {
        if (instance == null)
            instance = new XBeePort();

        return instance;
    }

    /**
     * Clear the response queue
     */
    public void clearResponseQueue() {
        cnx.clearResponseQueue();
    }

    /**
     * Close the connection
     */
    public void close(){
        cnx.close();
    }

    /**
     * Private constructor of XBeePort
     * @throws IOException
     */
    private XBeePort() throws IOException {
        // Build the buffer
        this.buffer = new XBeeBuffer();
        buffer.addObserver(this);

        // Read network configuration file
        File configurationFile = new File(NETWORK_CONFIG_FILE);
        JSONObject confJson = new JSONObject(Utils.loadConfigFile(configurationFile));

        String portName = confJson.getJSONObject("connection").getString("xbee");

        this.cnx = new XBee();


        // Get all the available ports on the computer.
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
            cnx.open(portName, DATA_RATE);
            cnx.addPacketListener(new PacketHandler());

        } catch (XBeeException e) {

        }
    }

    /**
     * Send a request
     * @param request Request to send
     * @return Response
     * @throws XBeeTimeoutException
     * @throws XBeeException
     */
    public XBeeResponse sendSynchronous(XBeeRequest request)
            throws XBeeTimeoutException,
            XBeeException {
        return cnx.sendSynchronous(request);
    }

    /**
     * Attach a listener
     * @param listener Listener
     */
    public void setListener(ControllerConnection.Listener listener) {
        this.listener = listener;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("[INFO] Received: " + arg);
        listener.messageReceived((String) arg);
    }

    /**
     * This class handles the reception of a packet
     */
    private class PacketHandler implements PacketListener {
        @Override
        public void processResponse(XBeeResponse response){

            if (response.getApiId() == ApiId.RX_16_RESPONSE) {
                buffer.put((RxResponse16) response);

            }
        }
    }

}
