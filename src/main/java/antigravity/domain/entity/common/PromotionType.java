package antigravity.domain.entity.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromotionType {

    COUPON(DiscountType.WON, "쿠폰사용시 현금할인"), CODE(DiscountType.PERCENT, "코드사용시 %할인");

    private final DiscountType discountType;
    private final String promotionType;

    public int applyDiscount(int originPrice, int discountValue, PromotionType promotionType) {
        int discountPrice = 0;

        if (COUPON.equals(promotionType)) {
            discountPrice = discountValue;
        } else if (CODE.equals(promotionType)) {
            discountPrice = originPrice * discountValue/100;
        }
        return discountPrice;
    }

}
