package antigravity.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;

@SpringBootTest
class ProductServiceTest {

    @Autowired ProductService productService;

    @Test
    @DisplayName("상품 가격 추출 테스트 - 할인 쿠폰 적용")
    void getProductAmountPromotionCoupon() {
        // given
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(2)
                .couponIds(new int[]{3})
                .build();

        // when
        ProductAmountResponse response = productService.getProductAmount(request);

        // then
        Assertions.assertEquals(response.getName(), "티셔츠", "상품 명 일치");
        Assertions.assertEquals(response.getOriginPrice(), 75000, "기존 금액 일치");
        Assertions.assertEquals(response.getDiscountPrice(), 10000, "할인 금액 일치");
        Assertions.assertEquals(response.getFinalPrice(), 65000, "최종 가격 일치");
    }

    @Test
    @DisplayName("상품 가격 추출 테스트 - 할인 코드 적용")
    void getProductAmountPromotionCode() {
        // given
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(2)
                .couponIds(new int[]{4})
                .build();

        // when
        ProductAmountResponse response = productService.getProductAmount(request);

        // then
        Assertions.assertEquals(response.getName(), "티셔츠", "상품 명 일치");
        Assertions.assertEquals(response.getOriginPrice(), 75000, "기존 금액 일치");
        Assertions.assertEquals(response.getDiscountPrice(), 8000, "할인 금액 일치");
        Assertions.assertEquals(response.getFinalPrice(), 67000, "최종 가격 일치");
    }

    @Test
    @DisplayName("상품 가격 추출 테스트 - 할인 쿠폰 + 할인 코드 적용")
    void getProductAmountPromotionAll() {
        // given
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(2)
                .couponIds(new int[]{3, 4, 5})
                .build();

        // when
        ProductAmountResponse response = productService.getProductAmount(request);

        // then
        Assertions.assertEquals(response.getName(), "티셔츠", "상품 명 일치");
        Assertions.assertEquals(response.getOriginPrice(), 75000, "기존 금액 일치");
        Assertions.assertEquals(response.getDiscountPrice(), 26000, "할인 금액 일치");
        Assertions.assertEquals(response.getFinalPrice(), 49000, "최종 가격 일치");
    }

    @Test
    @DisplayName("상품 가격 추출 테스트 - 쿠폰 미선택")
    void getProductAmountNoPromotion() {
        // given
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(2)
                .couponIds(new int[]{})
                .build();

        // when
        ProductAmountResponse response = productService.getProductAmount(request);

        // then
        Assertions.assertEquals(response.getName(), "티셔츠", "상품 명 일치");
        Assertions.assertEquals(response.getOriginPrice(), 75000, "기존 금액 일치");
        Assertions.assertEquals(response.getDiscountPrice(), 0, "할인 금액 일치");
        Assertions.assertEquals(response.getFinalPrice(), 75000, "최종 가격 일치");
    }

    @Test
    @DisplayName("상품 가격 추출 테스트 - 해당 상품에 적용할 수 없는 쿠폰 선택")
    void getProductAmountInvaildPromotion() {
        // given
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(2)
                .couponIds(new int[]{3, 1})
                .build();

        // when
        ProductAmountResponse response = productService.getProductAmount(request);

        // then
        Assertions.assertEquals(response.getName(), "티셔츠", "상품 명 일치");
        Assertions.assertEquals(response.getOriginPrice(), 75000, "기존 금액 일치");
        Assertions.assertEquals(response.getErrorCoupon(), "30000원 할인쿠폰", "에러 쿠폰 명 일치");
        Assertions.assertEquals(response.getErrorMsg(), "해당 상품에 적용할 수 없는 쿠폰입니다.", "에러 메시지 일치");
    }

    @Test
    @DisplayName("상품 가격 추출 테스트 - 아직 사용할 수 없는 쿠폰 선택")
    void getProductAmountNotYetAvailablePromotion() {
        // given
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(2)
                .couponIds(new int[]{3, 6})
                .build();

        // when
        ProductAmountResponse response = productService.getProductAmount(request);

        // then
        Assertions.assertEquals(response.getName(), "티셔츠", "상품 명 일치");
        Assertions.assertEquals(response.getOriginPrice(), 75000, "기존 금액 일치");
        Assertions.assertEquals(response.getErrorCoupon(), "15000원 할인쿠폰", "에러 쿠폰 명 일치");
        Assertions.assertEquals(response.getErrorMsg(), "해당 쿠폰은 아직 사용할 수 없습니다.", "에러 메시지 일치");
    }

    @Test
    @DisplayName("상품 가격 추출 테스트 - 기간이 만료된 쿠폰 선택")
    void getProductAmountExpiredPromotion() {
        // given
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(2)
                .couponIds(new int[]{3, 7})
                .build();

        // when
        ProductAmountResponse response = productService.getProductAmount(request);

        // then
        Assertions.assertEquals(response.getName(), "티셔츠", "상품 명 일치");
        Assertions.assertEquals(response.getOriginPrice(), 75000, "기존 금액 일치");
        Assertions.assertEquals(response.getErrorCoupon(), "20000원 할인쿠폰", "에러 쿠폰 명 일치");
        Assertions.assertEquals(response.getErrorMsg(), "해당 쿠폰은 기간이 만료되었습니다.", "에러 메시지 일치");
    }
}