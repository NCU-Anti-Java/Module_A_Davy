package io.github.anti_java.struct;

/**
 * Created by Davy on 2015/12/17.
 */
public class UIEvent {
    public enum Type {
        KEYBOARD
    }

    public enum Key {
        RIGHT,
        LEFT,
        UP,
        DOWN,
        SPACE,
        OTHER
    }

    private final Type mType;
    private final Object mData;

    public UIEvent(Type type, Object data) {
        mType = type;
        mData = data;
    }

    public Type getType() {
        return mType;
    }

    public Object getData() {
        return mData;
    }
}
