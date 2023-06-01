package antigravity.service.discount;

import antigravity.domain.Promotion;

public interface DiscountPolicy {
    int applyDiscount(int originPrice, Promotion promotion);
}
