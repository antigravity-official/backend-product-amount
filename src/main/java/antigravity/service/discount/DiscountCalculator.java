package antigravity.service.discount;

import antigravity.domain.Promotion;

public interface DiscountCalculator {
    int applyDiscount(int originPrice, Promotion promotion);
}
