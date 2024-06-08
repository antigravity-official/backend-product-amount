package antigravity.domain;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.exception.NotSupportedPromotionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static antigravity.domain.PromotionType.CODE;
import static antigravity.domain.PromotionType.COUPON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductWithPromotionsTest {

    @Test
    @DisplayName("프로모션을 적용하여 가격 정보를 계산")
    public void getAmountApplied() {
        // given
        int[] couponIds = {1, 2};
        Product product = Product.builder().id(1).name("product1").price(125000).build();
        ProductWithPromotions productWithPromotions = ProductWithPromotions.builder()
                .product(product)
                .promotions(List.of(
                        Promotion.builder().id(1).promotion_type(COUPON.name()).discount_value(3000).build(),
                        Promotion.builder().id(2).promotion_type(CODE.name()).discount_value(15).build()
                ))
                .build();

        int allDiscountPrice = 3000 + (125000 * 15 / 100);
        int realDiscountedPrice =  product.getPrice() - ((product.getPrice() - allDiscountPrice) / 1000 * 1000);

        // when
        ProductAmount amount = productWithPromotions.getAmountApplied(couponIds);

        // then
        assertThat(amount).isNotNull();
        assertThat(amount.getId()).isEqualTo(product.getId());
        assertThat(amount.getName()).isEqualTo(product.getName());
        assertThat(amount.getOriginPrice()).isEqualTo(product.getPrice());
        assertThat(amount.getDiscountPrice()).isEqualTo(allDiscountPrice);
        assertThat(amount.getFinalPrice()).isEqualTo(product.getPrice() - allDiscountPrice);

        assertThat(amount.getDisplayDiscountPrice()).isEqualTo(realDiscountedPrice);
        assertThat(amount.getDisplayFinalPrice()).isEqualTo(product.getPrice() - realDiscountedPrice);
    }

    @Test
    @DisplayName("프로모션을 적용하여 받을 수 있는 총 할인 금액의 최대는 원래 상품 가격")
    public void getAmountAppliedMax() {
        // given
        int[] couponIds = {1, 2};
        Product product = Product.builder().id(1).name("product1").price(10000).build();
        ProductWithPromotions productWithPromotions = ProductWithPromotions.builder()
                .product(product)
                .promotions(List.of(
                        Promotion.builder().id(1).promotion_type(COUPON.name()).discount_value(5000).build(),
                        Promotion.builder().id(2).promotion_type(CODE.name()).discount_value(50).build()
                ))
                .build();
        // when
        ProductAmount amount = productWithPromotions.getAmountApplied(couponIds);

        // then
        assertThat(amount).isNotNull();
        assertThat(amount.getId()).isEqualTo(product.getId());
        assertThat(amount.getName()).isEqualTo(product.getName());
        assertThat(amount.getOriginPrice()).isEqualTo(product.getPrice());
        assertThat(amount.getDiscountPrice()).isEqualTo(product.getPrice());
        assertThat(amount.getFinalPrice()).isEqualTo(0);

        // 보여지는 최종 금액의 최소 값은 1000, 최대 값은 10000000
        assertThat(amount.getDisplayDiscountPrice()).isEqualTo(product.getPrice() - 1000);
        assertThat(amount.getDisplayFinalPrice()).isEqualTo(1000);
    }

    @Test
    @DisplayName("해당 상품에 적용할 수 없는 프로모션을 적용한 경우 NotSupportedPromotionException 예외 발생")
    public void applyNotSupportedPromotion() {
        // given
        int[] couponIds = {1, 2};
        Product product = Product.builder().id(1).name("product1").price(125000).build();
        ProductWithPromotions productWithPromotions = ProductWithPromotions.builder()
                .product(product)
                .promotions(List.of(
                        Promotion.builder().id(1).promotion_type(COUPON.name()).discount_value(3000).build()
                ))
                .build();

        // when
        // then
        assertThrows(NotSupportedPromotionException.class, () -> productWithPromotions.getAmountApplied(couponIds));
    }

}