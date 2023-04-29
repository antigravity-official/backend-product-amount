package antigravity.exception.product;

import antigravity.enums.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class ProductNotFoundException extends RuntimeException {

	private final ExceptionCode exceptionCode = ExceptionCode.PRODUCT_NOT_FOUND;

}
