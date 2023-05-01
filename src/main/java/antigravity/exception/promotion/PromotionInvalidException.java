package antigravity.exception.promotion;

import antigravity.enums.exception.ExceptionInfo;
import lombok.Getter;

@Getter
public class PromotionInvalidException extends RuntimeException {
	private final ExceptionInfo exceptionInfo = ExceptionInfo.PROMOTION_INVALID;
}
