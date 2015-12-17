package io.github.anti_java.struct;

/**
 * Created by Davy on 2015/12/17.
 */
public class Item {
    private final String mName;
    private final int mId;
    private final boolean mShared;
    public boolean isReached;

    public Item(final String name, final int id, final boolean shared) {
        mName = name;
        mId = id;
        mShared = shared;
        isReached = false;
    }

    public String getName() {
        return mName;
    }

    public int getId() {
        return mId;
    }

    public boolean isShared() {
        return mShared;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Item)
            if (((Item) o).getId() == getId())
                return true;
        return false;
    }

    @Override
    public String toString() {
        return "Item {\n" +
                "  name: " + mName + ",\n" +
                "  id: " + mId + ",\n" +
                "  shared: " + mShared + "\n" +
                "}";
    }
}
