package antigravity.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.rmi.ServerException;

@Slf4j
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<String> serverExceptionHandler(ServerException ex, HttpServletRequest request) {

        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_GATEWAY);
    }
}
