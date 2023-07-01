package antigravity.fixture;

import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.domain.entity.common.DiscountType;
import antigravity.domain.entity.common.PromotionType;
import antigravity.repository.PromotionProductsRepository;
import java.time.LocalDate;

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
}
