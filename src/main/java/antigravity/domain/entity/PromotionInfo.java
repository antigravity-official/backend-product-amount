package antigravity.domain.entity;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

import static javax.persistence.EnumType.STRING;

@Embeddable
@Getter
public class PromotionInfo {
    @Enumerated(STRING)
    private PromotionType promotionType;
    @Enumerated(STRING)
    private DiscountType discount_type; // WON : 금액 할인, PERCENT : %할인
    private Double discount_value; // 할인 금액 or 할인 %

    protected PromotionInfo() {
    }

    private PromotionInfo(PromotionType promotionType, DiscountType discountType, Double discountValue) {
        this.promotionType = promotionType;
        this.discount_type = discountType;
        this.discount_value = discountValue;
    }

    public static PromotionInfo of(PromotionType promotionType, DiscountType discountType, Double discountValue) {
        return new PromotionInfo(promotionType, discountType, discountValue);
    }
}
