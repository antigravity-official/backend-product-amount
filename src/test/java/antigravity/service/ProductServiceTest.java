package antigravity.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import annotation.ServiceTest;
import antigravity.exceptions.OmittedRequireFieldException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 할인 적용시 성공")
    void When_ProductDiscount_Then_Success() {
        // given
        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder().productId(1).couponIds(new int[]{1, 2}).build();
        // when
        ProductAmountResponse productAmount = productService.getProductAmount(productInfoRequest);
        // then
        assertEquals("피팅노드상품", productAmount.getName());
        assertEquals(215000, productAmount.getOriginPrice());
        assertEquals(57750, productAmount.getDiscountPrice());
        assertEquals(157000, productAmount.getFinalPrice());
    }

    @Test
    @DisplayName("상품이 없을 경우 예외처리")
    void When_NoProduct_Then_ExceptionThrows() {
        // given
        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder().productId(2).couponIds(new int[]{1, 2}).build();
        // when
        String responseMessage = assertThrows(OmittedRequireFieldException.class, () -> productService.getProductAmount(productInfoRequest)).getMessage();
        // then
        assertEquals("요청하신 상품을 찾을 수 없습니다.", responseMessage);
    }

    @Test
    @DisplayName("상품에 적용된 프로모션이 아닐 경우 예외처리")
    void When_NoPromotionWithProduct_Then_ExceptionThrows() {
        // given
        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder().productId(1).couponIds(new int[]{3}).build();
        // when
        String responseMessage = assertThrows(OmittedRequireFieldException.class, () -> productService.getProductAmount(productInfoRequest)).getMessage();
        // then
        assertEquals("상품에 적용된 프로모션이 없습니다.", responseMessage);
    }

    @Test
    @DisplayName("상품에 적용된 프로모션을 일부 사용할 수 없을 경우 예외처리")
    void When_SomethingDoNotUsePromotionWithProduct_Then_ExceptionThrows() {
        // given
        ProductInfoRequest productInfoRequest = ProductInfoRequest.builder().productId(1).couponIds(new int[]{1,3}).build();
        // when
        String responseMessage = assertThrows(OmittedRequireFieldException.class, () -> productService.getProductAmount(productInfoRequest)).getMessage();
        // then
        assertEquals("적용할 수 없는 프로모션이 있습니다.", responseMessage);
    }
}