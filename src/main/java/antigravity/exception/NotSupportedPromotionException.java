package antigravity.exception;

public class NotSupportedPromotionException extends RuntimeException {
    private int couponId;

    public NotSupportedPromotionException(int couponId) {
        super(couponId + " 프로모션은 적용할 수 없습니다.");
    }
}
