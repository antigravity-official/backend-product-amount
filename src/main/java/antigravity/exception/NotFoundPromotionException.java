package antigravity.exception;

import antigravity.global.exception.ErrorMessage;

public class NotFoundPromotionException extends RuntimeException {
	public NotFoundPromotionException(ErrorMessage message) {
		super(message.getMessage());
	}
}
