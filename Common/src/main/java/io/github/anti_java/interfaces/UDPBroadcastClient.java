package io.github.anti_java.interfaces;

/**
 * UDP BroadCast client is a thread which loops forever to get information from
 * Centeralized Data Center and broadcast to all the client computer.
 */
public interface UDPBroadcastClient {
    /**
     * called by main program of server computer when the all the connection is established
     * and the networked game is started.
     * The method starts the UDP Broadcast thread.
     */
    void startUDPBroadCast() ;

//    The abstract behaviors of the thread

//    getClientIPTables();
//    processing the table ;
//    vector v = getUpdateInfo();
//    for each o in v
//    call o.toString() to encode v with ADD command ;
//    broadcast v to all the client computer
//    to add characters and item to Dynamic Object Module of client computer.
//    loop 5 times/per sec {
//        vector v = getUpdateInfo();
//        encode v with UPDATE command;
//        broadcast encoded v to all the
//        client computer
//    } forever
}
