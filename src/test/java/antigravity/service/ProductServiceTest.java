package antigravity.service;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("쿠폰 사용시 상품 금액에 대한 할인이 적용되어야 한다.")
    void getProductAmount() {

        //given
        int[] couponIds = {1, 2};

        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(couponIds)
                .build();

        //when
        ProductAmountResponse productAmount = productService.getProductAmount(productInfoRequest);

        //then
        assertAll(
                () -> assertEquals("피팅노드상품", productAmount.getName()),
                () -> assertEquals(215000, productAmount.getOriginPrice()),
                () -> assertEquals(152000, productAmount.getFinalPrice()),
                () -> assertEquals(63000, productAmount.getDiscountPrice()),
                () -> assertEquals(0, productAmount.getFinalPrice() % 1000, "최종 상품 금액은 천단위로 절삭해야 한다.")
        );
    }
}