package antigravity.exception;

import antigravity.global.exception.ErrorMessage;

public class InvalidDateException extends RuntimeException {
	public InvalidDateException(ErrorMessage message) {
		super(message.getMessage());
	}
}
