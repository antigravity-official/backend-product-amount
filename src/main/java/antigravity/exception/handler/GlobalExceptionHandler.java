package antigravity.exception.handler;

import antigravity.exception.EntityIsEmptyException;
import antigravity.exception.EntityIsInvalidException;
import antigravity.exception.EntityNotFoundException;
import antigravity.exception.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZonedDateTime;

/**
 * Global exception handler for handling both custom and predefined exceptions.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles cases where an entity is not found in the database.
     * This exception typically occurs when a lookup for a specific entity
     * by its ID or other identifying attributes yields no result.
     *
     * @param ex The encountered EntityNotFoundException.
     * @return A ResponseEntity with the error details and BAD_REQUEST status.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        log.error("EntityNotFoundException: " + ex.getMessage());
        ErrorResponse errorResponse = buildErrorResponse(ex, ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles cases where an entity is identified as empty using static EMPTY.
     * This exception is commonly thrown when an operation is attempted on an entity
     * that is expected to have certain values or properties, but is found to be EMPTY.
     *
     * @param ex The encountered EntityIsEmptyException.
     * @return A ResponseEntity with the error details and BAD_REQUEST status.
     */
    @ExceptionHandler(EntityIsEmptyException.class)
    public ResponseEntity<Object> handleEntityIsEmpty(EntityIsEmptyException ex) {
        log.error("EntityIsEmptyException: " + ex.getMessage());
        ErrorResponse errorResponse = buildErrorResponse(ex, ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles cases where an entity is considered invalid, such as invalid field values or states.
     * This exception can occur in situations like validation failures or if business logic determines
     * that the entity's state or data is not acceptable for processing.
     *
     * @param ex The encountered EntityIsInvalidException.
     * @return A ResponseEntity with the error details and BAD_REQUEST status.
     */
    @ExceptionHandler(EntityIsInvalidException.class)
    public ResponseEntity<Object> handleEntityIsInvalid(EntityIsInvalidException ex) {
        log.error("EntityIsInvalidException: " + ex.getMessage());
        ErrorResponse errorResponse = buildErrorResponse(ex, ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Object>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles database access exceptions, which occur when there is a failure in interacting with the database.
     * This could be due to connectivity issues, query syntax errors, constraint violations, etc.
     *
     * @param ex The encountered DataAccessException.
     * @return A ResponseEntity with the error details and INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDataAccessException(DataAccessException ex) {
        String errorMessage = "Invalid SQL Statement";
        log.error("DataAccessException: " + errorMessage);
        ErrorResponse errorResponse = buildErrorResponse(ex, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<Object>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles illegal argument exceptions, which occur when there is an invalid field in
     * the initialization of an object. This could be due to DiscountType and PromotionType
     * type initializations being invalid.
     *
     * @param ex The encountered DataAccessException.
     * @return A ResponseEntity with the error details and INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        String errorMessage = "IllegalArgumentException: ";
        log.error("IllegalArgumentException: " + errorMessage);
        ErrorResponse errorResponse = buildErrorResponse(ex, errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<Object>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Builds an ErrorResponse object based on the given exception and HTTP status.
     *
     * @param ex The exception that was thrown.
     * @param httpStatus The HTTP status associated with the exception.
     * @return An ErrorResponse object.
     */
    private ErrorResponse buildErrorResponse(RuntimeException ex, String message, HttpStatus httpStatus) {
        return ErrorResponse.builder()
                .message(message)
                .cause(ex.getCause())
                .httpStatus(httpStatus)
                .timestamp(ZonedDateTime.now())
                .build();
    }
}