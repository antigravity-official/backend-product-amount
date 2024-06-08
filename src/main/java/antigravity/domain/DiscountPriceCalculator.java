package antigravity.domain;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.exception.InvalidDiscountPriceException;

public final class DiscountPriceCalculator {
    public static int apply(Product product, Promotion promotion) {
        PromotionType promotionType = PromotionType.of(promotion.getPromotion_type());

        int originalPrice = product.getPrice();
        int discountPrice = promotion.getDiscount_value();

        if (discountPrice < 0) throw new InvalidDiscountPriceException(discountPrice);

        switch (promotionType) {
            case COUPON -> {
                return applyCoupon(originalPrice, discountPrice);
            }
            case CODE -> {
                return applyCode(originalPrice, discountPrice);
            }
        }

        return 0;
    }

    private static int applyCoupon(int originalPrice, int discountPrice) {
        return originalPrice - discountPrice;
    }

    private static int applyCode(int originalPrice, int discountPrice) {
        return originalPrice - (originalPrice * discountPrice / 100);
    }
}
