package io.github.anti_java.exceptions;

/**
 * Created by Davy on 2015/12/17.
 */
public class RepeatedCharacterException extends OperationError {
    public RepeatedCharacterException(final int clientno) {
        super("Repeated client character: " + clientno);
    }
}
