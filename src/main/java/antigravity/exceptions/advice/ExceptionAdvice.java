package antigravity.exceptions.advice;

import antigravity.exceptions.OmittedRequireFieldException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    private ResponseEntity<String> newResponse(Throwable throwable, HttpStatus status){
        return this.newResponse(throwable.getMessage(), status);
    }

    private ResponseEntity<String> newResponse(String message, HttpStatus status){
        HttpHeaders headers= new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(message,headers,status);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<String> defaultException(Exception e){
        log.error("default Exception :{} ", e.getMessage());
        e.printStackTrace();
        return this.newResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({OmittedRequireFieldException.class, EmptyResultDataAccessException.class})
    protected ResponseEntity<String> handleOmittedRequireFieldException(OmittedRequireFieldException omittedRequireFieldException){
        return this.newResponse(omittedRequireFieldException, HttpStatus.FORBIDDEN);
    }
}
