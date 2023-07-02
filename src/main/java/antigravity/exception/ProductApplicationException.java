package antigravity.exception;

import antigravity.exception.code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductApplicationException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public ProductApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ProductApplicationException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        if(message == null) {
            return errorCode.getMessage();
        }
        return message;
    }
}
