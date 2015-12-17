package io.github.anti_java.interfaces;

/**
 * UDP Update Server is a thread which loops forever to get message from UDP
 * broadcast client and update the data in Dynamic Object Module.
 */
public interface UDPUpdateServer {
    /**
     * called by main program of client computer.
     * This method starts the main thread to receive message from UDP broadcast client.
     */
    void initUDPServer();

    /**
     * abstracted thread loop behavior

     loop forever {
         receive message from UDP broadcast client
         decode the message
         if (msg command is ADD)
         call addVirutalCharacter or addItem to Dynamic Object Module
         if (msg¡¦s command is UPDATE)
         call updateVirtualCharacter or updateItem to Dynamic Object Module
     }
     */
}
