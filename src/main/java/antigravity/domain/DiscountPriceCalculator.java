package antigravity.domain;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;

public final class DiscountPriceCalculator {
    public static int apply(Product product, Promotion promotion) {
        PromotionType promotionType = PromotionType.of(promotion.getPromotion_type());
        switch (promotionType) {
            case COUPON -> {
                return applyCoupon(product, promotion);
            }
            case CODE -> {
                return applyCode(product, promotion);
            }
        }

        return 0;
    }

    private static int applyCoupon(Product product, Promotion promotion) {
        return promotion.getDiscount_value();
    }

    private static int applyCode(Product product, Promotion promotion) {
        return product.getPrice() * promotion.getDiscount_value() / 100;
    }
}
