package antigravity.exception.product;

import antigravity.enums.exception.ExceptionInfo;
import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {
	private final ExceptionInfo exceptionInfo = ExceptionInfo.PRODUCT_NOT_FOUND;
}
