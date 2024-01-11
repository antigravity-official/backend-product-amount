package antigravity.exception;

import lombok.AllArgsConstructor;

/**
 * Handles cases where an entity is considered invalid, such as invalid field values or states.
 * This exception can occur in situations like validation failures or if business logic determines
 * that the entity's state or data is not acceptable for processing.
 */
@AllArgsConstructor
public class EntityIsInvalidException extends RuntimeException {

    public EntityIsInvalidException(String message) {
        super(message);
    }

    public EntityIsInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
