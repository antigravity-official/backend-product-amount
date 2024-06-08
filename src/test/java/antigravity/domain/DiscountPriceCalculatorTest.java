package antigravity.domain;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.exception.InvalidDiscountPriceException;
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
        assertThat(discountedPrice).isEqualTo(originalPrice - discountPrice);
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
        assertThat(discountedPrice).isEqualTo(originalPrice - (originalPrice * discountPrice / 100));
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

    @Test
    @DisplayName("할인 금액이 음수인 경우 InvalidDiscountPriceException 예외 발생")
    public void discountMinus() {
        // given
        Product product = Product.builder().id(1).name("product1").price(10000).build();
        Promotion promotion = Promotion.builder().id(1).promotion_type("COUPON").discount_value(-15000).build();

        // when
        // then
        assertThrows(InvalidDiscountPriceException.class, () -> DiscountPriceCalculator.apply(product, promotion));
    }

}