package antigravity.domain.enums;

public enum PromotionType {
    COUPON, CODE;

    public static PromotionType of(String promotionType) {
        for (PromotionType value : values()) {
            if(value.name().equals(promotionType)) {
                return value;
            }
        }
        return COUPON;
    }
}
