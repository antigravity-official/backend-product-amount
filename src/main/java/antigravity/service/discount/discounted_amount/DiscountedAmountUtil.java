package antigravity.service.discount.discounted_amount;

import antigravity.domain.Promotion;

public interface DiscountedAmountUtil {
    int getDiscountedValue(int originPrice, Promotion promotion);
    boolean isDiscountAmountValid(Promotion promotion);
}
