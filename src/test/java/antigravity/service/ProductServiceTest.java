package antigravity.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품 가격 추출 성공 - 금액 + 퍼센티지 할인")
    void getProductAmount() {
        //given
        int productId = 1;
        int[] couponIds = {1, 2};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(couponIds).build();

        //when
        ProductAmountResponse response = productService.getProductAmount(request);

        //then
        assertEquals(response.getOriginPrice(), 215_000);
        assertEquals(response.getDiscountPrice(), 58_000);
        assertEquals(response.getFinalPrice(), 157_000);
    }

    @Test
    @DisplayName("상품 가격 추출 성공 - 퍼센티지 할인")
    void getProductAmountCodeDiscount() {
        //given
        int productId = 1;
        int[] couponIds = {2};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(couponIds).build();

        //when
        ProductAmountResponse response = productService.getProductAmount(request);

        //then
        assertEquals(response.getOriginPrice(), 215_000);
        assertEquals(response.getDiscountPrice(), 33_000);
        assertEquals(response.getFinalPrice(), 182_000);
    }

    @Test
    @DisplayName("상품 가격 추출 성공 - 금액 할인")
    void getProductAmountCouponDiscount() {
        //given
        int productId = 1;
        int[] couponIds = {1};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(couponIds).build();

        //when
        ProductAmountResponse response = productService.getProductAmount(request);

        //then
        assertEquals(response.getOriginPrice(), 215_000);
        assertEquals(response.getDiscountPrice(), 30_000);
        assertEquals(response.getFinalPrice(), 185_000);
    }

    @Test
    @DisplayName("상품 가격 추출 성공 - 할인 미적용")
    void getProductAmountNoDiscount() {
        //given
        int productId = 1;
        int[] couponIds = {};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(couponIds).build();

        //when
        ProductAmountResponse response = productService.getProductAmount(request);

        //then
        assertEquals(response.getOriginPrice(), 215_000);
        assertEquals(response.getDiscountPrice(), 0);
        assertEquals(response.getFinalPrice(), 215_000);
    }

    @Test
    @DisplayName("상품 가격 추출 실패 - 최소 상품가격")
    void getProductAmountFailInvalidPriceMin() {
        //given
        int productId = 2;
        int[] couponIds = {1, 2};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(couponIds).build();

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("상품가격 오류로 인해 판매가 불가한 상품입니다.");
    }

    @Test
    @DisplayName("상품 가격 추출 실패 - 최대 상품가격")
    void getProductAmountFailInvalidPriceMax() {
        //given
        int productId = 3;
        int[] couponIds = {1, 2};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(couponIds).build();

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("상품가격 오류로 인해 판매가 불가한 상품입니다.");
    }

    @Test
    @DisplayName("상품 가격 추출 실패 - 할인적용 후 최소가격")
    void getProductAmountFailFinalPriceMin() {
        //given
        int productId = 4;
        int[] couponIds = {1};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(couponIds).build();

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("상품 최소가격 이하로는 프로모션 적용이 불가합니다.");
    }

    @Test
    @DisplayName("상품 가격 추출 실패 - 프로모션 아이디 중복")
    void getProductAmountFailDuplicatedCouponId() {
        //given
        int productId = 1;
        int[] couponIds = {1, 1};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(couponIds).build();

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("프로모션은 중복으로 적용될 수 없습니다.");
    }

    @Test
    @DisplayName("상품 가격 추출 실패 - 프로모션 기간(from)")
    void getProductAmountFailInvalidPromotionPeriodFrom() {

        //given
        int productId = 1;
        int[] couponIds = {1, 3};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(couponIds).build();

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("프로모션 적용 기간이 아닙니다.");
    }

    @Test
    @DisplayName("상품 가격 추출 실패 - 프로모션 기간(to)")
    void getProductAmountFailInvalidPromotionPeriodTo() {
        //given
        int productId = 1;
        int[] couponIds = {1, 4};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(couponIds).build();

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("프로모션 적용 기간이 아닙니다.");
    }

    @Test
    @DisplayName("상품 가격 추출 실패 - 상품 아이디 미조회")
    void getProductAmountFailNotExistsProduct() {
        //given
        int productId = -1;
        int[] couponIds = {1, 2};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(couponIds).build();

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("구매 가능한 상품정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상품 가격 추출 실패 - 프로모션 미조회")
    void getProductAmountFailNotExistsPromotion() {
        //given
        int productId = 1;
        int[] couponIds = {-1, 2};
        ProductInfoRequest request = ProductInfoRequest.builder()
            .productId(productId)
            .couponIds(couponIds).build();

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("적용 가능한 프로모션이 존재하지 않습니다.");
    }
}