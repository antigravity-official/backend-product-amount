package antigravity.fixture;

import antigravity.domain.entity.PromotionProducts;

public class PromotionProductFixture {
    public static PromotionProducts getPromotionProducts() {
        return PromotionProducts.builder()
            .promotion(PromotionFixture.getPromotion())
            .product(ProductFixture.getProduct())
            .build();
    }

    public static PromotionProducts getExpiredPeriodPromotionProducts() {
        return PromotionProducts.builder()
            .promotion(PromotionFixture.getExpiredPromotionPeriod())
            .product(ProductFixture.getProduct())
            .build();
    }

    public static PromotionProducts getMinPricePromotionProducts() {
        return PromotionProducts.builder()
            .promotion(PromotionFixture.getPromotionPrice())
            .product(ProductFixture.getProduct())
            .build();
    }

    public static PromotionProducts getMaxPricePromotionProducts() {
        return PromotionProducts.builder()
            .promotion(PromotionFixture.getPromotionPrice())
            .product(ProductFixture.getMaxProduct())
            .build();
    }
}
