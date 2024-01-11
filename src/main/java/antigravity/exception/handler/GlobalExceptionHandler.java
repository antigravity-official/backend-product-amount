package antigravity.exception.handler;

import antigravity.exception.EntityIsEmptyException;
import antigravity.exception.EntityIsInvalidException;
import antigravity.exception.EntityNotFoundException;
import antigravity.exception.ErrorResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        ErrorResponse errorResponse = buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityIsEmptyException.class)
    public ResponseEntity<Object> handleEntityIsEmpty(EntityIsEmptyException ex) {
        ErrorResponse errorResponse = buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityIsInvalidException.class)
    public ResponseEntity<Object> handleEntityIsInvalid(EntityIsInvalidException ex) {
        ErrorResponse errorResponse = buildErrorResponse(ex, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
        ErrorResponse errorResponse = buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<Object>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse buildErrorResponse(RuntimeException ex, HttpStatus httpStatus) {
        return new ErrorResponse(
                ex.getMessage(),
                ex.getCause(),
                httpStatus,
                ZonedDateTime.now()
        );
    }
}