package antigravity.domain;

import antigravity.domain.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductAmountTest {

    @Test
    @DisplayName("할인 금액 구하기")
    public void discount() {
        // given
        int discountPrice = 5000;
        Product product = Product.builder().id(1).name("product1").price(10000).build();
        ProductAmount amount = new ProductAmount(product);

        // when
        amount.discount(discountPrice);

        // then
        assertThat(amount).isNotNull();
        assertThat(amount.getId()).isEqualTo(product.getId());
        assertThat(amount.getName()).isEqualTo(product.getName());
        assertThat(amount.getOriginPrice()).isEqualTo(product.getPrice());
        assertThat(amount.getDiscountPrice()).isEqualTo(discountPrice);
        assertThat(amount.getFinalPrice()).isEqualTo(product.getPrice() - discountPrice);

        assertThat(amount.getDisplayDiscountPrice()).isEqualTo(discountPrice);
        assertThat(amount.getDisplayFinalPrice()).isEqualTo(product.getPrice() - discountPrice);
    }

    @Test
    @DisplayName("계산된 할인 금액이 원래 금액보다 커도 최대 할인 금액은 상품의 원래 가격")
    public void discountAll() {
        // given
        int discountedPrice = -5000;
        Product product = Product.builder().id(1).name("product1").price(10000).build();
        ProductAmount amount = new ProductAmount(product);

        // when
        amount.discount(discountedPrice);

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

}