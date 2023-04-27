package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;


class ProductServiceTest {
    // given
    Product product1 = Product.builder()
            .id(1)
            .name("피팅노드상품")
            .price(10000)
            .build();

    Promotion promotionCoupon = Promotion.builder()
            .id(1)
            .promotion_type("COUPON")
            .name("1000원 할인쿠폰")
            .discount_type("WON")
            .discount_value(1000)
            .use_started_at(new Date(2023, Calendar.APRIL, 25))
            .use_ended_at(new Date(2023, Calendar.APRIL, 27))
            .build();

    Promotion promotionCode = Promotion.builder()
            .id(2)
            .promotion_type("CODE")
            .name("10% 할인코드")
            .discount_type("PERCENT")
            .discount_value(10)
            .use_started_at(new Date(2023, Calendar.APRIL, 25))
            .use_ended_at(new Date(2023, Calendar.APRIL, 27))
            .build();

    PromotionProducts promotionProducts1 = PromotionProducts.builder()
            .id(1)
            .promotionId(1)
            .productId(1)
            .build();

    PromotionProducts promotionProducts2 = PromotionProducts.builder()
            .id(2)
            .promotionId(2)
            .productId(1)
            .build();

    List<Promotion> promotions = new ArrayList<>() {
        {
            add(promotionCoupon);
            add(promotionCode);
        }
    };

    @Test
    @DisplayName("쿠폰의 적용기간에 현재 날짜가 포함된다면 true를 리턴한다.")
    void checkCouponPeriodAvailable() {
        // given
        Date now = new Date(2023, Calendar.APRIL, 26);

        // when
        boolean actual = promotionCoupon.getUse_started_at().before(now) && promotionCoupon.getUse_ended_at().after(now);

        // then
        Assertions.assertTrue(actual);
    }

    @Test
    @DisplayName("10000원 상품 가격에 1000원 할인 쿠폰을 적용하면 9000원이 리턴된다.")
    void calculateDiscountPriceWonType() {
        // given
        int price = product1.getPrice();
        int discountValue = promotionCoupon.getDiscount_value();

        // when
        int actual = price - discountValue;

        // then
        Assertions.assertEquals(9000, actual);
    }

    @Test
    @DisplayName("10000원 상품 가격에 10% 할인 쿠폰을 적용하면 9000원이 리턴된다.")
    void calculateDiscountPricePercentType() {
        // given
        int price = product1.getPrice();
        int discountValue = promotionCode.getDiscount_value();

        // when
        int actual = price * (100 - discountValue) / 100;

        // then
        Assertions.assertEquals(9000, actual);
    }

    @Test
    void getProductAmount() {
        // given
        promotions.sort((o1, o2) -> {
            if (o1.getDiscount_type().equals("WON") && !o2.getDiscount_type().equals("WON")) {
                // o1이 "WON"인 경우, o1이 o2보다 우선순위가 높음
                return -1;
            } else if (!o1.getDiscount_type().equals("WON") && o2.getDiscount_type().equals("WON")) {
                // o2가 "WON"인 경우, o2가 o1보다 우선순위가 높음
                return 1;
            } else {
                // o1과 o2의 type이 같은 경우, 기존의 순서를 유지함
                return 0;
            }
        });


        // when
        int discountPrice = getDiscountPrice(product1, promotions);


        // then
        Assertions.assertEquals(8000, discountPrice);
    }

    int getDiscountPrice(Product product, List<Promotion> promotions) {
        int discountPrice = product.getPrice();

        for (Promotion promotion : promotions) {
            if (promotion.getDiscount_type() == "WON") {
                discountPrice -= promotion.getDiscount_value();
            } else if (promotion.getDiscount_type() == "PERCENT") {
                discountPrice = discountPrice * (100 - promotion.getDiscount_value()) / 100;
            }
        }
        return discountPrice / 1000 * 1000;
    }
}