package io.github.anti_java.interfaces;

import io.github.anti_java.struct.IPAddress;

/**
 * Created by Davy on 2015/12/15.
 */
public interface TCPClient {
    /**
     * Called by main program of a client computer to initialize the connection with server
     * This function should use the server ip to connect the server
     * if the connection succeed, the function return true
     * it return false if the connection is failed
     */
    boolean connectServer(IPAddress serverip);

    /**
     * called by User Interface Module or dynamic object module
     * recall that in this design, the client machine is treated as a input
     * processing machine. User Interface Module processes input from keyboard and mouse
     * and then translate the event into a MoveCode and pass to TCP client
     * Currently, you only need to implement the following MoveCode
     *      TURNEAST - (passed by User Interface Module) the main character turn east
     *      TURNSOUTH - ..
     *      TURNNORTH
     *      TURNWEST
     *      GET - (passed by dynamic object module) the virtual character is near an item
     *            and decide to grab it
     *            Dynamic Object Module decide the grab action is possible
     *            and then send the movecode ¡§GET¡¨ to TCP client
     * Once TCP client module receives a message, it wraps it into a message and
     * transmit to TCP server module via the established connection.
     */
    void inputMoves(int MoveCode);
}
