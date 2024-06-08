package antigravity.domain;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.exception.PromotionTypeNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscountPriceCalculatorTest {

    @Test
    @DisplayName("Coupon 프로모션을 적용하면 금액 단위 할인")
    public void applyCoupon() {
        // given
        int originalPrice = 215000;
        int discountPrice = 30000;
        Product product = Product.builder().id(1).name("product1").price(originalPrice).build();
        Promotion promotion = Promotion.builder().id(1).promotion_type("COUPON").discount_value(discountPrice).build();

        // when
        int discountedPrice = DiscountPriceCalculator.apply(product, promotion);

        // then
        assertThat(discountedPrice).isEqualTo(discountPrice);
    }

    @Test
    @DisplayName("Code 프로모션을 적용하면 퍼센트(%) 단위 할인")
    public void applyCode() {
        // given
        int originalPrice = 215000;
        int discountPrice = 15;
        Product product = Product.builder().id(1).name("product1").price(originalPrice).build();
        Promotion promotion = Promotion.builder().id(1).promotion_type("CODE").discount_value(discountPrice).build();

        // when
        int discountedPrice = DiscountPriceCalculator.apply(product, promotion);

        // then
        assertThat(discountedPrice).isEqualTo(originalPrice * discountPrice / 100);
    }

    @Test
    @DisplayName("Coupon, Code 프로모션 타입이 아닌 경우 NotFoundPromotionType 예외 발생")
    public void applyOtherType() {
        // given
        int originalPrice = 215000;
        int discountPrice = 15;
        Product product = Product.builder().id(1).name("product1").price(originalPrice).build();
        Promotion promotion = Promotion.builder().id(1).promotion_type("OTHER").discount_value(discountPrice).build();

        // when
        // then
        assertThrows(PromotionTypeNotFoundException.class, () -> DiscountPriceCalculator.apply(product, promotion));
    }

    // TODO 추가 고려 사항
    // 1. 제품의 가격보다 프로모션으로 할인 받으려는 금액이 더 큰 경우

}