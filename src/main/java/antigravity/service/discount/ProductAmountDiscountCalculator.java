package antigravity.service.discount;

import antigravity.domain.Promotion;

// todo 오류 뭐냐??
public interface ProductAmountDiscountCalculator {
    int applyDiscount(int originPrice, Promotion promotion);
    boolean validatePromotionDiscountValue(Promotion promotion);
}
