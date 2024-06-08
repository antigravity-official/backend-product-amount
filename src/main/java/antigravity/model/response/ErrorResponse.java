package antigravity.model.response;

import lombok.Getter;

import static antigravity.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@Getter
public class ErrorResponse {
    private String code;
    private String message;

    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse createInternalServerError() {
        return new ErrorResponse(INTERNAL_SERVER_ERROR.getCode(), INTERNAL_SERVER_ERROR.getDefaultMessage());
    }
}