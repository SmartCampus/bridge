package fr.unice.smart_campus.cnx.xbee;

import com.rapplogic.xbee.api.XBeeAddress16;
import com.rapplogic.xbee.api.wpan.RxResponse16;
import com.rapplogic.xbee.util.ByteUtils;

import java.util.*;

/**
 * Message Buffer for XBee
 * Created by Cyril Cecchinel on 10/07/2014.
 */
public class XBeeBuffer extends Observable {

    private Map<XBeeAddress16, ArrayList<String>> buffer;

    /**
     * Default constructor
     */
    public XBeeBuffer(){
        this.buffer = new HashMap<XBeeAddress16, ArrayList<String>>();
    }

    /**
     * Put a response into the message
     * @param r  Response
     */
    public void put(RxResponse16 r){
        ArrayList<String> list;
        // Retrieve the corresponding  list
         if (buffer.containsKey(r.getRemoteAddress())){
             list = buffer.get(r.getRemoteAddress());
         } else {
             // No list found. Build a new one
            list = new ArrayList<String>();
         }

        // Put the incoming response
        String receivedMessage = ByteUtils.toString(r.getData());
        list.add(receivedMessage);
        buffer.put(r.getRemoteAddress(),list);

        // If toAdd contains "\n" or "\r" then notify observers and send full message
        if ((receivedMessage.contains("\n")) || (receivedMessage.contains("\r"))){
            String fullMessage = getMessage(r.getRemoteAddress());
            // Flush buffer for the current remote address
            flush(r.getRemoteAddress());

            setChanged();
            notifyObservers(fullMessage);
        }


    }

    /**
     * Build a message buffered
     * @param addr Source address
     * @return Message send by the source
     */
    public String getMessage(XBeeAddress16 addr){
        if (!buffer.containsKey(addr))
            throw new NoSuchElementException("No messages buffered for" + addr);

        List<String> messageList = buffer.get(addr);
        StringBuffer b = new StringBuffer();
        for (String s: messageList)
            b.append(s);

        return b.toString();
    }

    /**
     * Flush the source buffer
     * @param addr Source address
     */
    public void flush(XBeeAddress16 addr){
        if (buffer.containsKey(addr))
            buffer.put(addr, new ArrayList<String>());
    }

    @Override
    public String toString(){
        return buffer.toString();
    }

}
