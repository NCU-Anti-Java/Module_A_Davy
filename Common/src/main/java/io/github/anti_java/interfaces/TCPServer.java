package io.github.anti_java.interfaces;

import io.github.anti_java.struct.IPAddress;

import java.util.List;

/**
 * Created by Davy on 2015/12/15.
 */
public interface TCPServer {
    /* called by main program of server computer to start TCP server
     * and begin to listen connections from client computers.
     * The server should maintain a table to keep the ip addresss of
     * client computers which connects to this server.
     */
    void initTCPServer();

    /* called by UDP broadcast client
     * After all the connections are established UDP broadcast client
     * may starts to broadcast.
     * Before it starts to broadcast, it will need the IP addresses of
     * the client computer.
     * This method return all the IP addresses of client computer
     * and put them in a List
     */
    List<IPAddress> getClientIPTable();
}
