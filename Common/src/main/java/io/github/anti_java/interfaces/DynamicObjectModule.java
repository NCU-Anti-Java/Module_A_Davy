package io.github.anti_java.interfaces;

import io.github.anti_java.struct.Position;

import java.util.List;

/**
 * This module keeps a copy of data from Centeralized Data Center.
 * Its main function is to be read by rendering engine to draw pictures/frames.
 * Please also read Centeralized Data Center.
 * However, the data structure in Dynamic Object Module is different from Centeralized Data Center.
 * Centeralized Data Center only care (X,Y), DIR, SPEED.
 * However, in this module, these attributes are only part of the attributes of sprite class.
 * A sprite class (as in your prototype) contains other attributes like sprite images and etc.
 */
public interface DynamicObjectModule {
    /**
     * called by UDPUC to add a main virtual character for the client computer clientno
     * in the module programming exercise, a virtual character has the following basic
     * attributes (you can extend in the future)
     * x.y ¡V current pposition
     * dir ¡V direction the virtual character is heading
     * speed ¡V the moving speed
     * You should create a sprite class and initialize its attributes like (x,y), dir, speed
     */
    void addVirtualCharacter(int clientno);

    /**
     * called by UDP update server to create an shared item
     * An item is can be indexed by a name and an index.
     * if shared is true, the item can only be own by a client at any time
     * if shared is false, the item can be obtained by any client as if it can reappear
     * when it is obtained by a virtual character (¨Ò¦p«æ±Ï¥])
     * In this function, you should create a sprite class which contain attributes like name, index, and shared
     */
    void addItem(String name, int index, boolean shared);

    /**
     * called by UDP update server
     * update the data of a virtual character
     */
    void updateItem(int index, boolean shared, int owner);

    /**
     * called by UDP update server
     * update the data of an item
     */
    void updateVirtualCharacter(int clientno, int dir, int speed, int x, int y);

    /**
     * called by sprite render engine
     * this method return a vector which contains the references of all the dynamic objects which should drawn.
     */
    List<Object> getAllDynamicObjects();

    /**
     * called by Scene Render Engine
     * This function returns the coordinates of the virtual character controlled by this client computer.
     * The position (x,y) is the location on the map.
     * It is used to compute the view port and decide which part of the map should be displayed in the view port.
     */
    Position getVirtualCharacterXY();

    /**
     * called by User Interface Module
     * When User Interface Module accepts an keyboard input and it is a GET key it calls this method.
     * This method should determine if the GET action is possible by comparing the
     * virtual character¡¦s position and any item nearby.
     * If the GET action is possible, it should call inputMoves(GET) of TCP Client Module
     */
    void keyGETPressed();
}
