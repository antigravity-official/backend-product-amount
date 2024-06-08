package antigravity.exception;

public class NotFoundPromotionType extends RuntimeException {
    private String promotionType;

    public NotFoundPromotionType(String promotionType) {
        super("지원하지 않는 프로모션 코드입니다. TYPE: " + promotionType);
        this.promotionType = promotionType;
    }
}
