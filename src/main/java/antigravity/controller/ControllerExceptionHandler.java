package antigravity.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import antigravity.exception.NotAvailableDatePromotionException;
import antigravity.exception.NotFoundResourceException;

@RestControllerAdvice
public class ControllerExceptionHandler {

	private final static String GLOBAL_EXCEPTION_MESSAGE = "잘못된 요청입니다. 요청 url : %s, 에러 메세지 : %s";

	@ExceptionHandler(NotFoundResourceException.class)
	public ResponseEntity<String> handleNotFoundResourceException(NotFoundResourceException e) {
		return ResponseEntity.badRequest()
			.body(e.getMessage());
	}

	@ExceptionHandler(NotAvailableDatePromotionException.class)
	public ResponseEntity<String> handleNotAvailableDatePromotionException(NotAvailableDatePromotionException e) {
		return ResponseEntity.badRequest()
			.body(e.getMessage());
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<String> handleRuntimeException(
		HttpServletRequest request, RuntimeException e) {
		return ResponseEntity.badRequest()
			.body(String.format(GLOBAL_EXCEPTION_MESSAGE,e.getMessage(),request.getRequestURI()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(
		HttpServletRequest request, Exception e) {
		return ResponseEntity.badRequest()
			.body(String.format(GLOBAL_EXCEPTION_MESSAGE,e.getMessage(),request.getRequestURI()));
	}

}
