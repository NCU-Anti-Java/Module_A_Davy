package io.github.anti_java.interfaces;

import io.github.anti_java.exceptions.InvalidCharacterException;
import io.github.anti_java.exceptions.OperationError;
import io.github.anti_java.exceptions.RepeatedCharacterException;

import java.util.List;

/**
 * The Centeralized Data Center keeps the centralized and unique data of dynamic objects.
 * In this module programming project we only concern two kinds of dynamic objects.
 * They includes virtual character (每一個 client 控制的角色) and shared items.
 * In the future, for your own project and goal, you need to add more types of object into data center.
 */
public interface CentralizedDataCenter {
    /**
     * A map is stored in the server, this map specifies the initial location of each virtual character.
     * We assume there are maximum four virtual characters in the maps.
     * So when the map is read, these initial locations are stored somewhere.
     * When a client connects to TCP Server Module, TCP Server Module should call this method to
     * create a virtual character in the Centralized Data Center.
     * The initial location of this virtual character should use the initial locations stored in the map.
     * In the module programming exercise, a virtual character has the following basic attributes
     * (you can extend in the future):
     *     x.y – current position
     *     dir – direction the virtual character is heading
     *     velocity – the moving speed
     * Initially, the dir and velocity should be zero.
     */
    void addVirtualCharacter(int clientno) throws RepeatedCharacterException;

    /**
     * There is a map stored in the server.
     * When the map is loaded, the map contains the information of all the items on the map.
     * When these information is read this method is called to create an shared item at
     * position x, y.
     * An item can be indexed by a name or an index.
     * If shared is true, the item can only be owned by a client at any time
     * If shared is false, the item can be obtained by any client as if it can reappear
     * after it is obtained by a virtual character.
     */
    void addItem(String name, int index, boolean shared, int x, int y);

    /**
     * called by TCP Server Module
     * when TCP Server Module receives a MoveCode which is “TURN” from TCP Client Module,
     * it call this function to change the moving direction of virtual character of clientno
     */
    void updateDirection(int clientno, int MoveCode) throws InvalidCharacterException;

    /**
     * called by TCP Server Module
     * when TCP Server Module receives a MoveCode which is a “GET”, TCP Server Module calls this method.
     * This method should check if there is an item ahead of the virtual character
     * clientno’s direction and if the item is within reaching range.
     * If the item is within reaching range, check if the item is a shared object.
     * If it is a shared object, check if it is already owned by any virtual character.
     * Finally, change the states of the item accordingly.
     */
    void getItem(int clientno) throws InvalidCharacterException;

    /**
     * called by UDP broadcast client
     * The method will return a vector, which contains all the references to the dynamic objects
     * (virtual character and item) which has just been updated recently.
     * These object should contain a toString() method when it is called,
     * its attributes will be formatted into a string.
     */
    List<Object> getUpdateInfo();

    /**
     * called by TCP Server Module, after all the connections are established and the game is started
     * this method start the following thread to update each virtual character’s x,y
     * every 0.5 second.
     */
    void startUpdatingThread();

//    abstract behaviors of the thread
//    loop {
//        sleep for 0.5 second
//        for each virtual character in Centeralized Data Center {
//            compute new x,y according to dir and speed
//        }
//    }
}
