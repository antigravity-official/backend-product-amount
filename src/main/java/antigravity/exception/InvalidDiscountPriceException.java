package antigravity.exception;

import static antigravity.exception.ErrorCode.INVALID_DISCOUNT_PRICE;

public class InvalidDiscountPriceException extends ProductAmountBusinessException {
    private final ErrorCode errorCode = INVALID_DISCOUNT_PRICE;
    private int price;

    public InvalidDiscountPriceException(int price) {
        super("유효하지 않은 할인 금액입니다. price = " + price);
    }

    @Override
    public String getErrorCode() {
        return errorCode.getCode();
    }

    @Override
    public String getErrorMessage() {
        return getMessage() == null || getMessage().isBlank() ? errorCode.getDefaultMessage() : getMessage();
    }
}
