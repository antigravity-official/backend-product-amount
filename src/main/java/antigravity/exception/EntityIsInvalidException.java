package antigravity.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class EntityIsInvalidException extends RuntimeException {

    public EntityIsInvalidException(String message) {
        super(message);
    }

    public EntityIsInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
