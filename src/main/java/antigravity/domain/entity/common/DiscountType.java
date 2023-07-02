package antigravity.domain.entity.common;

import antigravity.exception.ProductApplicationException;
import antigravity.exception.code.PromotionErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountType {

    WON("금액 할인"), PERCENT("비율 할인");

    private final String description; //설명

    public int applyDiscount(int originPrice, int discountValue, DiscountType discountType) {
        int discountPrice = 0;

        if (WON.equals(discountType)) {
            discountPrice = discountValue;
        } else if (PERCENT.equals(discountType)) {
            discountPrice = originPrice * discountValue/100;
        }

        if (originPrice < discountPrice) {
            throw new ProductApplicationException(PromotionErrorCode.EXCEED_ORIGIN_PRICE);
        }

        return discountPrice;
    }

}
