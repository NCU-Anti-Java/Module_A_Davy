package io.github.anti_java.struct;

import io.github.anti_java.Global;
import rx.Observable;
import rx.observables.StringObservable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Davy on 2015/12/17.
 */
public class Character {
    public Position position;
    public int direction;
    public int velocity;
    public List<Item> items;

    public Character() {
        position = new Position(0, 0);
        direction = Global.DEFAULT_DIR;
        velocity = 0;
        items = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Character {\n" +
                "  position: {x: " + position.x + ", y: " + position.y + "},\n" +
                "  direction: " + direction + ",\n" +
                "  velocity: " + velocity + ",\n" +
                "  items: [" + StringObservable.join(Observable.from(items).map(Item::toString), ", ") + "]\n" +
                "}";

    }
}
