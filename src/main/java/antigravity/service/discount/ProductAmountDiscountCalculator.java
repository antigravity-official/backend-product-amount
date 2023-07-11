package antigravity.service.discount;

import antigravity.domain.Promotion;

public interface ProductAmountDiscountCalculator {
    int applyDiscount(int originPrice, Promotion promotion);
    boolean isDiscountAmountValid(Promotion promotion);
}
