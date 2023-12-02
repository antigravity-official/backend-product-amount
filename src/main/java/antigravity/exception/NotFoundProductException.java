package antigravity.exception;

import antigravity.global.exception.ErrorMessage;

public class NotFoundProductException extends RuntimeException {
	public NotFoundProductException(ErrorMessage message) {
		super(message.getMessage());
	}
}
