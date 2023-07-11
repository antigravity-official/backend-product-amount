package antigravity.error;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {

    private final String message;
    private final int status;
    private final String code;
    private final LocalDateTime timestamp;

    private ErrorResponse(
            String message,
            int status,
            String code
    ) {
        this.message = message;
        this.status = status;
        this.code = code;
        this.timestamp = LocalDateTime.now();
    }


    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code.getMessage(), code.getStatus(), code.getCode());
    }
}
