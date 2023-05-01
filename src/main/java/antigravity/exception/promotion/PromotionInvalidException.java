package antigravity.exception.promotion;

import antigravity.enums.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class PromotionInvalidException extends RuntimeException {
	private final ExceptionCode exceptionCode = ExceptionCode.PROMOTION_INVALID;
}
