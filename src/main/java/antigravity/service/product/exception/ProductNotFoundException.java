package antigravity.service.product.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("해당 상품이 존재하지 않습니다.");
    }
}
