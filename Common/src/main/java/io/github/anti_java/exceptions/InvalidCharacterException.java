package io.github.anti_java.exceptions;

/**
 * Created by Davy on 2015/12/17.
 */
public class InvalidCharacterException extends OperationError {
    public InvalidCharacterException(final int clientNo) {
        super("Invalid character of client: " + clientNo);
    }
}
