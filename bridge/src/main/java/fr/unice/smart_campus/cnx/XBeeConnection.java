package fr.unice.smart_campus.cnx;

import com.rapplogic.xbee.api.*;
import com.rapplogic.xbee.api.wpan.RxResponse16;
import com.rapplogic.xbee.api.wpan.TxRequest16;
import com.rapplogic.xbee.api.wpan.TxStatusResponse;
import gnu.io.CommPortIdentifier;

import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by cyrilcecchinel on 01/07/2014.
 */
public class XBeeConnection implements ControllerConnection {

    /** Default bits per second for CMD port */
    private static final int DATA_RATE = 9600;

    /** The xbee port */
    private XBee port;

    /** The port we are normally going to use */
    private String portName;

    /** Connection listener */
    private Listener listener = null;

    /** Xbee destination address */
    private XBeeAddress16 destination = null;

    public XBeeConnection(String portN, XBeeAddress16 destination) throws Exception {
        this.portName = portN;
        this.port = new XBee();
        this.destination = destination;

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

        port.open(portN, DATA_RATE);

        port.addPacketListener(new PacketHandler());

    }
    @Override
    public void setConnectionListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void sendMessage(String str) throws IOException, InterruptedException {
        int[] payload = toIntArray(str);

        // Build request
        TxRequest16 tx = new TxRequest16(destination, payload);

        // Send request and wait for status response
        try {
            TxStatusResponse status = (TxStatusResponse) port.sendSynchronous(tx);
            if (!status.isSuccess())
                System.err.println("No response received");
        } catch (XBeeException e) {
            System.err.println("Xbee exception");
            e.printStackTrace();
        }



    }

    @Override
    public void close() {
        port.close();
    }

    /**
     * Build int array from string
     * @param str String
     * @return Int array
     */
    private int[] toIntArray(String str) {
        int[] tab = new int[str.length()];
        for (int i = 0; i<str.length(); i++)
            tab[i] = str.charAt(i);
        return tab;
    }

    /**
     * Build a string from int array
     * @param b Int array
     * @return String
     */
    public String buildStringFromInts(int[] b) {
        StringBuffer strb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            strb.append((char) b[i]);
        return strb.toString();
    }


    private class PacketHandler implements PacketListener{

        @Override
        public void processResponse(XBeeResponse response){
            if (response.getApiId() == ApiId.RX_16_RESPONSE) {
                RxResponse16 rx = (RxResponse16) response;

                // get data
                String inputLine = buildStringFromInts(rx.getData());

                if ((listener != null) && (inputLine != null))
                    listener.messageReceived(inputLine);
        }
    }


    }
}
