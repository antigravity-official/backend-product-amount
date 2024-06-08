package antigravity.exception;

import static antigravity.exception.ErrorCode.PROMOTION_TYPE_NOT_FOUND;

public class PromotionTypeNotFoundException extends ProductAmountBusinessException {
    private final ErrorCode errorCode = PROMOTION_TYPE_NOT_FOUND;
    private String promotionType;

    public PromotionTypeNotFoundException(String promotionType) {
        super("지원하지 않는 프로모션 코드입니다. TYPE: " + promotionType);
        this.promotionType = promotionType;
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
