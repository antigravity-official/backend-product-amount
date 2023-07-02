package antigravity.fixture;

import antigravity.domain.entity.PromotionProducts;

public class PromotionProductFixture {
    public static PromotionProducts getInValidPromotionProducts() {
        return PromotionProducts.builder()
            .id(1)
            .promotion(PromotionFixture.getPromotion())
            .product(ProductFixture.getProduct2())
            .build();
    }

    public static PromotionProducts getExpiredPeriodPromotionProducts() {
        return PromotionProducts.builder()
            .id(2)
            .promotion(PromotionFixture.getExpiredPeriodPromotion())
            .product(ProductFixture.getProduct())
            .build();
    }

    public static PromotionProducts getMinPricePromotionProducts() {
        return PromotionProducts.builder()
            .id(3)
            .promotion(PromotionFixture.getMinPricePromotion())
            .product(ProductFixture.getProduct())
            .build();
    }

    public static PromotionProducts getMaxPricePromotionProducts() {
        return PromotionProducts.builder()
            .id(4)
            .promotion(PromotionFixture.getPromotion2())
            .product(ProductFixture.getMaxProduct())
            .build();
    }

    public static PromotionProducts getExceedPromotionPriceProducts() {
        return PromotionProducts.builder()
            .id(5)
            .promotion(PromotionFixture.getExceedPricePromotion())
            .product(ProductFixture.getProduct())
            .build();
    }
}
