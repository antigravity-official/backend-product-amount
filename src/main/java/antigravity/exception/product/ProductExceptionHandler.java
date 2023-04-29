package antigravity.exception.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import antigravity.enums.exception.ExceptionCode;
import antigravity.model.response.exception.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ProductExceptionHandler {

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@ExceptionHandler(ProductNotFoundException.class)
	public ExceptionResponse productNotFoundException(final ProductNotFoundException e) {
		final ExceptionCode exceptionCode = e.getExceptionCode();
		log.error("{} - code: {}, message: {}", e.getClass(), exceptionCode.getCode(), exceptionCode.getMessage());
		return ExceptionResponse.of(exceptionCode);
	}

}
