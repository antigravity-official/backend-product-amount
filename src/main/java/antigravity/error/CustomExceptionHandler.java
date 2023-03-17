package antigravity.error;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import antigravity.error.dto.ErrorDto;
import antigravity.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorDto> handleBusinessException(BusinessException ex) {

        log.error("BusinessException -> ", ex);
        return ResponseEntity
                .status(ex.getErrorType().getStatus())
                .body(
                        new ErrorDto(ex.getMessage())
                );
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorDto> handleException(Exception ex) {
        log.error("Exception -> ", ex);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorDto(ex.getMessage())
                );
    }

}