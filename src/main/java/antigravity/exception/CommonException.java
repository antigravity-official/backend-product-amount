package antigravity.exception;

import antigravity.enums.ErrorResponse;

public class CommonException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private ErrorResponse response;

    public CommonException(ErrorResponse response) {
        super(response.getMessage());
        this.response = response;
    }

    public ErrorResponse getErrorCode() {
        return response;
    }
}
