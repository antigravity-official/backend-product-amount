package antigravity.constrants;

import lombok.Getter;

@Getter
public enum PromotionConstrants {

    COUPON("COUPON", "WON"),
    CODE("CODE", "PERCENT")
    ;

    private String promotionType;
    private String discountType;

    PromotionConstrants(String promotionType, String discountType) {
        this.promotionType = promotionType;
        this.discountType = discountType;
    }

}
