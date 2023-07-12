package antigravity.service.discount.discountedamount;

import antigravity.domain.Promotion;

public interface DiscountedAmountUtil {
    int getDiscountedValue(int originPrice, Promotion promotion);
    boolean isDiscountAmountValid(Promotion promotion);
}
