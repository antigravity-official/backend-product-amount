package antigravity.testutils;

import antigravity.constants.DiscountType;
import antigravity.constants.PromotionType;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;

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
                .promotion_type(PromotionType.COUPON)
                .name("30000원 할인쿠폰")
                .discount_type(DiscountType.WON)
                .discount_value(30000)
                .use_started_at(LocalDate.of(2022, 11, 1))
                .use_ended_at(LocalDate.of(2024, 3, 1))
                .build();
    }

    public static ProductInfoRequest buildSampleProductInfoRequest() {
        int[] couponIds = {1, 2};
        return ProductInfoRequest.builder()
                .productId(1)
                .couponIds(couponIds)
                .build();
    }

    public static ProductAmountResponse buildExpectedResponseFromSample() {
        return ProductAmountResponse.builder()
                .name("피팅노드상품")
                .originPrice(215000)
                .discountPrice(62250)
                .finalPrice(153000)
                .build();
    }
}