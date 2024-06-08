package antigravity.exception;

import antigravity.model.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductAmountBusinessException.class)
    public ResponseEntity<ErrorResponse> handle(ProductAmountBusinessException ex) {
        return ResponseEntity
                .status(400)
                .body(new ErrorResponse(ex.getErrorCode(), ex.getErrorMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception ex) {
        return ResponseEntity
                .status(500)
                .body(ErrorResponse.createInternalServerError());
    }
}
