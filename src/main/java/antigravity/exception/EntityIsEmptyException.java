package antigravity.exception;

import lombok.AllArgsConstructor;

/**
 * Handles cases where an entity is identified as empty using static EMPTY.
 * This exception is commonly thrown when an operation is attempted on an entity
 * that is expected to have certain values or properties, but is found to be EMPTY.
 */
@AllArgsConstructor
public class EntityIsEmptyException extends RuntimeException {

    public EntityIsEmptyException(String message) {
        super(message);
    }

    public EntityIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
