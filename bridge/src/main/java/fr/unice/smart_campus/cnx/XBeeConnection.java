package fr.unice.smart_campus.cnx;

import com.rapplogic.xbee.api.*;
import com.rapplogic.xbee.api.wpan.RxResponse16;
import com.rapplogic.xbee.api.wpan.TxRequest16;
import com.rapplogic.xbee.api.wpan.TxStatusResponse;
import com.rapplogic.xbee.util.ByteUtils;
import fr.unice.smart_campus.cnx.xbee.XBeePort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;

import java.io.IOException;
import java.util.Enumeration;

/**
 * This class represents a XBee connection
 * Created by cyrilcecchinel on 01/07/2014.
 */
public class XBeeConnection implements ControllerConnection {



    /** The port we are normally going to use */
    private String portName;

    /** Xbee destination address */
    private XBeeAddress16 destination = null;

    private XBeePort port;

    /** Prevent duplicate messages */
    private String echoPrevention = "";

    /**
     * Build a XBeeCpnnection
     * @param destination Destination address
     * @throws IOException
     */
    public XBeeConnection(XBeeAddress16 destination) throws IOException {
        this.destination = destination;
        this.port = XBeePort.getInstance();

    }
    @Override
    public void setConnectionListener(Listener listener) {
       port.setListener(listener);
    }

    @Override
    public void sendMessage(String str) throws IOException, InterruptedException {
        int[] payload = ByteUtils.stringToIntArray(str);
        // Build request
        TxRequest16 tx = new TxRequest16(destination, payload);
        System.out.println("Sending " + str + "...");
        // Send request and wait for status response
        try {
            TxStatusResponse status = null;
            while (status == null || !status.isSuccess())
                 status = (TxStatusResponse) port.sendSynchronous(tx);


        } catch (XBeeException e) {
            System.err.println("XBee exception");
            //e.printStackTrace();
        }

        port.clearResponseQueue();



    }

    @Override
    public void close() {
        port.close();
    }





}
