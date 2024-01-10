package antigravity.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EntityIsEmptyException extends RuntimeException {

    public EntityIsEmptyException(String message) {
        super(message);
    }

    public EntityIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
