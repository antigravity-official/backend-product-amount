package antigravity.exception;

public abstract class ProductAmountBusinessException extends RuntimeException {
    public ProductAmountBusinessException() {
    }

    public ProductAmountBusinessException(String message) {
        super(message);
    }

    public abstract String getErrorCode();
    public abstract String getErrorMessage();
}
