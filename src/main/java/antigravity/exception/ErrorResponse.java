package antigravity.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse<T> {

    private String resultCode;

    public static ErrorResponse<Void> error(String errorCode) {
        return new ErrorResponse<>(errorCode);
    }
}
