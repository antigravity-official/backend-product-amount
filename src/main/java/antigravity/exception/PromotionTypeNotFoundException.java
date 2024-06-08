package antigravity.exception;

public class PromotionTypeNotFoundException extends RuntimeException {
    private String promotionType;

    public PromotionTypeNotFoundException(String promotionType) {
        super("지원하지 않는 프로모션 코드입니다. TYPE: " + promotionType);
        this.promotionType = promotionType;
    }
}
