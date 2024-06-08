package antigravity.exception;

import static antigravity.exception.ErrorCode.NOT_SUPPORTED_PROMOTION;

public class NotSupportedPromotionException extends ProductAmountBusinessException {
    private final ErrorCode errorCode = NOT_SUPPORTED_PROMOTION;
    private int couponId;

    public NotSupportedPromotionException(int couponId) {
        super(couponId + " 프로모션은 적용할 수 없습니다.");
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
