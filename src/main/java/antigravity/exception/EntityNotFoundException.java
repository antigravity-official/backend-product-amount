package antigravity.exception;

import lombok.AllArgsConstructor;

/**
 * Handles cases where an entity is not found in the database.
 * This exception typically occurs when a lookup for a specific entity
 * by its ID or other identifying attributes yields no result.
 */
@AllArgsConstructor
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
