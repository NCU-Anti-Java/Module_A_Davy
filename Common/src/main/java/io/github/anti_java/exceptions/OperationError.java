package io.github.anti_java.exceptions;

/**
 * Created by Davy on 2015/12/17.
 */
public class OperationError extends Exception {
    private final String mMessage;

    public OperationError(final String msg) {
        mMessage = msg;
    }

    @Override
    public String getMessage() {
        return mMessage;
    }

    @Override
    public String toString() {
        return mMessage;
    }
}
