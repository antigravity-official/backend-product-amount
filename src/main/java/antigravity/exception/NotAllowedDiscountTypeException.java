package antigravity.exception;

import antigravity.global.exception.ErrorMessage;

public class NotAllowedDiscountTypeException extends RuntimeException {
	public NotAllowedDiscountTypeException(ErrorMessage message) {
		super(message.getMessage());
	}
}
