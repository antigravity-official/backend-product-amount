package antigravity.exception;

import antigravity.global.exception.ErrorMessage;

public class NotAllowedAmountRangeException extends RuntimeException {
	public NotAllowedAmountRangeException(ErrorMessage message) {
		super(message.getMessage());
	}
}
