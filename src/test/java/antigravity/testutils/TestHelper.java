package antigravity.testutils;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;

import java.time.LocalDate;

public class TestHelper {
    public static Product buildSampleProduct() {
        return Product.builder()
                .id(1)
                .name("피팅노드상품")
                .price(215000)
                .build();
    }

    public static PromotionProducts buildSamplePromotionProduct() {
        return PromotionProducts.builder()
                .id(1)
                .promotionId(1)
                .productId(1)
                .build();
    }

    public static Promotion buildSamplePromotion() {
        return Promotion.builder()
                .id(1)
                .promotion_type("COUPON")
                .name("30000원 할인쿠폰")
                .discount_type("WON")
                .discount_value(30000)
                .use_started_at(LocalDate.of(2022, 11, 1))
                .use_ended_at(LocalDate.of(2023, 3, 1))
                .build();
    }
}