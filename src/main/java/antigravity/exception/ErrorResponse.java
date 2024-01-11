package antigravity.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@Builder
public class ErrorResponse {
    private final String message;
    private final Throwable cause;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;
}