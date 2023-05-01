package antigravity.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import antigravity.model.response.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CommonExceptionHandler {

	//PathVariable validation 탈락
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public ExceptionResponse constraintViolationException(ConstraintViolationException e) {
		log.error("{} : {}", e.getClass(), e.getMessage());
		return ExceptionResponse.of(HttpStatus.BAD_REQUEST.value(), e.getMessage());
	}

}
