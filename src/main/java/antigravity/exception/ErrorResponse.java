package antigravity.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@Builder
public class ErrorResponse {
    private final String message;           // 예외명
    private final Throwable cause;          // 예외 이유
    private final HttpStatus httpStatus;    // 콜 Status
    private final ZonedDateTime timestamp;  // 예외 발생 시간
}