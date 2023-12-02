package antigravity.exception;

import antigravity.global.exception.ErrorMessage;

public class NotFoundPromotionProductsException extends RuntimeException {
	public NotFoundPromotionProductsException(ErrorMessage message) {
		super(message.getMessage());
	}
}
