package antigravity.service.discount.discountAmountCalculator;

import antigravity.domain.Promotion;

public interface ProductAmountDiscountCalculator {
    int applyDiscount(int originPrice, Promotion promotion);
    boolean isDiscountAmountValid(Promotion promotion);
}
