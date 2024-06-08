package antigravity.exception;

import static antigravity.exception.ErrorCode.PRODUCT_NOT_FOUND;

public class ProductNotFoundException extends ProductAmountBusinessException {
    private final ErrorCode errorCode = PRODUCT_NOT_FOUND;
    private int productId;

    public ProductNotFoundException(int productId) {
        super("존재하지 않는 상품입니다. id = " + productId);
        this.productId = productId;
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
