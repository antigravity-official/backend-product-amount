package antigravity.domain.entity;

import antigravity.exception.CommonException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    @DisplayName("천단위 절삭")
    void getPrice() {

        // given
        Product product = Product.builder()
                .id(1)
                .name("test")
                .price(123456)
                .build();

        // when

        // then
        Assertions.assertEquals(123000, product.getPrice());

    }

    @Test
    @DisplayName("범위 지정 예외 - minimum")
    void minPrice() {

        // given
        Product p = Product.builder()
                .id(1)
                .name("test")
                .price(1)
                .build();

        // when
        Throwable exception = assertThrows(CommonException.class, () -> {
            p.getPrice();
        });

        String message = exception.getMessage();

        // then
        org.assertj.core.api.Assertions.assertThat("상품 금액이 최소 금액 10000 보다 작습니다.").isEqualTo(message);

    }

    @Test
    @DisplayName("범위 지정 예외 - maximum")
    void maxPrice() {

        // given
        Product p = Product.builder()
                .id(1)
                .name("test")
                .price(10000001)
                .build();

        // when
        Throwable exception = assertThrows(CommonException.class, () -> {
            p.getPrice();
        });

        String message = exception.getMessage();

        // then
        org.assertj.core.api.Assertions.assertThat("상품 금액이 최대 금액 10000000 보다 큽니다.").isEqualTo(message);

    }


}