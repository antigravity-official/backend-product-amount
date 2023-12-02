package antigravity.global.exception;


import static antigravity.global.exception.ErrorMessage.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import antigravity.exception.InvalidDateException;
import antigravity.exception.NotAllowedAmountRangeException;
import antigravity.exception.NotAllowedDiscountTypeException;
import antigravity.exception.NotFoundProductException;
import antigravity.exception.NotFoundPromotionException;
import antigravity.exception.NotFoundPromotionProductsException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(NotFoundProductException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundProductException(NotFoundProductException exception) {
		log.error("NotFoundProductException : ", exception);
		return ErrorResponse.toResponseEntity(NOT_FOUND_PRODUCT);
	}

	@ExceptionHandler(NotFoundPromotionException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundPromotion(NotFoundPromotionException exception) {
		log.error("NotFoundPromotionException : ", exception);
		return ErrorResponse.toResponseEntity(NOT_FOUND_PROMOTION);
	}

	@ExceptionHandler(NotFoundPromotionProductsException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundPromotionProducts(NotFoundPromotionProductsException exception) {
		log.error("NotFoundPromotionProductsException : ", exception);
		return ErrorResponse.toResponseEntity(NOT_FOUND_PROMOTION_PRODUCTS);
	}

	@ExceptionHandler(NotAllowedDiscountTypeException.class)
	public ResponseEntity<ErrorResponse> handleNotAllowedDiscountType(NotAllowedDiscountTypeException exception) {
		log.error("NotAllowedDiscountTypeException : ", exception);
		return ErrorResponse.toResponseEntity(NOT_ALLOWED_DISCOUNT_TYPE);
	}

	@ExceptionHandler(NotAllowedAmountRangeException.class)
	public ResponseEntity<ErrorResponse> handleNotAllowedAmountRange(NotAllowedAmountRangeException exception) {
		log.error("NotAllowedAmountRangeException : ", exception);
		return ErrorResponse.toResponseEntity(NOT_ALLOWED_AMOUNT_RANGE);
	}

	@ExceptionHandler(InvalidDateException.class)
	public ResponseEntity<ErrorResponse> handleInvalidDate(InvalidDateException exception) {
		log.error("InvalidDateException : ", exception);
		return ErrorResponse.toResponseEntity(INVALID_DATE);
	}
}
