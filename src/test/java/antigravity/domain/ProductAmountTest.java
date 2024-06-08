package antigravity.domain;

import antigravity.domain.entity.Product;
import antigravity.exception.InvalidDiscountPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        int discountPrice = 15000;
        Product product = Product.builder().id(1).name("product1").price(10000).build();
        ProductAmount amount = new ProductAmount(product);

        // when
        amount.discount(discountPrice);

        // then
        assertThat(amount).isNotNull();
        assertThat(amount.getId()).isEqualTo(product.getId());
        assertThat(amount.getName()).isEqualTo(product.getName());
        assertThat(amount.getOriginPrice()).isEqualTo(product.getPrice());
        assertThat(amount.getDiscountPrice()).isEqualTo(product.getPrice());
        assertThat(amount.getFinalPrice()).isEqualTo(0);

        assertThat(amount.getDisplayDiscountPrice()).isEqualTo(product.getPrice());
        assertThat(amount.getDisplayFinalPrice()).isEqualTo(0);
    }

    @Test
    @DisplayName("할인 금액이 음수인 경우 InvalidDiscountPriceException 예외 발생")
    public void discountMinus() {
        // given
        int discountPrice = -15000;
        Product product = Product.builder().id(1).name("product1").price(10000).build();
        ProductAmount amount = new ProductAmount(product);

        // when
        // then
        assertThrows(InvalidDiscountPriceException.class, () -> amount.discount(discountPrice));
    }

}