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
    @DisplayName("상품 가격 추출 성공 - 금액 + 퍼센트 할인")
    void getProductAmount() {
        System.out.println("상품 가격 추출 테스트");
        //given
        int productId = 1;
        int[] couponIds = {1, 2};
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(productId)
                .couponIds(couponIds).build();

        //when
        Throwable exception = assertThrows(RuntimeException.class, () -> productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("프로모션 중 적용가능한 것이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상품 가격 추출 성공 - 퍼센트 할인")
    void getProductAmountCodeDiscount() {
        //given
        int productId = 1;
        int[] couponIds = {2};
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(productId)
                .couponIds(couponIds).build();

        //when
        Throwable exception = assertThrows(RuntimeException.class, () ->  productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("해당 프로모션의 사용가능 기간이 만료되었습니다.");
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
        Throwable exception = assertThrows(RuntimeException.class, () -> productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("프로모션 중 적용가능한 것이 존재하지 않습니다.");
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
        assertEquals(response.getOriginPrice(), 215000);
        assertEquals(response.getDiscountPrice(), 0);
        assertEquals(response.getFinalPrice(), 215000);
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
        assertThat(exception.getMessage()).isEqualTo("상품가격 오류 상품입니다.");
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
        assertThat(exception.getMessage()).isEqualTo("상품가격 오류 상품입니다.");
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
        assertThat(exception.getMessage()).isEqualTo("프로모션 중 적용가능한 것이 존재하지 않습니다.");
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
        assertThat(exception.getMessage()).isEqualTo("프로모션 중 적용가능한 것이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상품 가격 추출 실패 - 프로모션 기간")
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
        assertThat(exception.getMessage()).isEqualTo("프로모션 중 적용가능한 것이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상품 가격 추출 실패 - 프로모션 기간")
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
        assertThat(exception.getMessage()).isEqualTo("프로모션 중 적용가능한 것이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상품 가격 추출 실패 - 상품 아이디 조회 불가")
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
    @DisplayName("상품 가격 추출 실패 - 프로모션 조회 불가")
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
        assertThat(exception.getMessage()).isEqualTo("프로모션 중 적용가능한 것이 존재하지 않습니다.");
    }
}