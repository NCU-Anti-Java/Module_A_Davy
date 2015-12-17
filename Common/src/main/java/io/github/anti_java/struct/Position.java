package io.github.anti_java.struct;

/**
 * Created by Davy on 2015/12/17.
 */
public class Position {
    public int x, y;

    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        return x * 1000000 + y;
    }

    @Override
    public boolean equals(Object o) {
        if (! (o instanceof Position))
            return false;

        final Position pos = (Position) o;
        return pos.x == x && pos.y == y;
    }
}
