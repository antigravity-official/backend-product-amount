package antigravity.service.discount;

import antigravity.domain.entity.Promotion;

public interface DiscountPolicy {
    int applyDiscount(int originPrice, Promotion promotion);
}
