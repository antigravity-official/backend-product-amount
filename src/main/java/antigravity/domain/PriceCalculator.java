package antigravity.domain;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.exception.NotFoundPromotionType;

public final class PriceCalculator {
    public static int apply(Product product, Promotion promotion) {
        switch (promotion.getPromotion_type()) {
            case "COUPON" -> {
                return applyCoupon(product, promotion);
            }
            case "CODE" -> {
                return applyCode(product, promotion);
            }
        }

        throw new NotFoundPromotionType(promotion.getPromotion_type());
    }

    private static int applyCoupon(Product product, Promotion promotion) {
        return promotion.getDiscount_value();
    }

    private static int applyCode(Product product, Promotion promotion) {
        return product.getPrice() * promotion.getDiscount_value() / 100;
    }
}
