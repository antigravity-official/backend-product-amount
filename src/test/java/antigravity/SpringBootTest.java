package antigravity;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.domain.PromotionProducts;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import antigravity.dto.response.ProductAmountResponse;
import antigravity.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static antigravity.error.ErrorCode.*;
import static antigravity.global.ProductFixture.*;
import static antigravity.global.PromotionFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("[SpringBootTest] ProductService Test")
class SpringBootTest extends ServiceTestSupport {

    @Autowired
    private ProductService productService;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private Product product5;
    private Product product6;

    @BeforeEach
    void createValidProductsAndPromotions() {
        product1 = productRepository.save(VALID_PRODUCT1.toEntity()); // 상품 1 -> 158,500 원
        product2 = productRepository.save(VALID_PRODUCT2.toEntity()); // 상품 2 -> 178,300 원
        product3 = productRepository.save(VALID_PRODUCT3.toEntity()); // 상품 3 -> 353,900 원
        product4 = productRepository.save(VALID_PRODUCT4.toEntity()); // 상품 4 -> 464,960 원
        product5 = productRepository.save(VALID_PRODUCT5.toEntity()); // 상품 5 -> 720,060 원
        product6 = productRepository.save(LOW_PRICE_PRODUCT.toEntity()); // 상품 6 -> 45,000 원
        Promotion promotion1 = promotionRepository.save(VALID_PROMOTION1.toEntity()); // 프로모션 1 -> 30,000원 정액 할인
        Promotion promotion2 = promotionRepository.save(VALID_PROMOTION2.toEntity()); // 프로모션 2 -> 15% 정률 할인
        Promotion promotion3 = promotionRepository.save(VALID_PROMOTION3.toEntity()); // 프로모션 3 -> 35% 정률 할인
        Promotion promotion4 = promotionRepository.save(VALID_PROMOTION4.toEntity()); // 프로모션 4 -> 53,000원 정액 할인
        // 상품 1 -> 프로모션 1 매핑
        promotionProductsRepository.save(
                PromotionProducts.of(promotion1.getId(), product1.getId()));
        // 상품 1 -> 프로모션 2 매핑
        promotionProductsRepository.save(
                PromotionProducts.of(promotion2.getId(), product1.getId()));
        // 상품 2 -> 프로모션 2 매핑
        promotionProductsRepository.save(
                PromotionProducts.of(promotion2.getId(), product2.getId()));
        // 상품 2 -> 프로모션 3 매핑
        promotionProductsRepository.save(
                PromotionProducts.of(promotion3.getId(), product2.getId()));
        // 상품 3 -> 프로모션 3 매핑
        promotionProductsRepository.save(
                PromotionProducts.of(promotion3.getId(), product3.getId()));
        // 상품 4 -> 프로모션 4 매핑
        promotionProductsRepository.save(
                PromotionProducts.of(promotion4.getId(), product4.getId()));
        // 상품 5 -> 프로모션 1 매핑
        promotionProductsRepository.save(
                PromotionProducts.of(promotion1.getId(), product5.getId()));
        // 상품 5 -> 프로모션 4 매핑
        promotionProductsRepository.save(
                PromotionProducts.of(promotion4.getId(), product5.getId()));
    }

    /**
     * 정상 성공 TestCase 1
     * 최초 판매가격 : 158,500원
     * 할인 정책 : (1) 최초 판매가 기준 15% 정률 + (2) 30,000원 정액 할인
     * - 정률 할인 15% -> 23,775원 할인
     * - 정액 할인 30,000원
     * - 프로모션 총 할인 53,775원
     * - 절삭 전 금액 : 158,500 - 53,775 = 104,725원
     * - 절삭 금액 : 725원
     * - 최종 할인 금액 (프로모션 총 할인 + 절삭 금액) = 53,775 + 725 = 54,500원
     * - 최종 금액 = 158,500 - 54,500 = 104,000원
     */
    @Test
    @DisplayName("[Product] - 할인 정책 정상 적용 테스트 1 - 다중 할인 [정률 + 정액]")
    void getValidProductAmount1() {
        // given - @BeforeEach createValidProductsAndPromotions
        // when
        ProductAmountResponse response = productService.getProductAmount(product1.getId());
        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(product1.getName()),
                () -> assertThat(response.getOriginPrice()).isEqualTo(product1.getPrice()),
                () -> assertThat(response.getDiscountPrice()).isEqualTo(54500),
                () -> assertThat(response.getFinalPrice()).isEqualTo(104000)
        );
    }

    /**
     * 정상 성공 TestCase 2
     * 최초 판매가격 : 178,300원
     * 할인 정책 : (1) 최초 판매가 기준 15% 정률 + (2) 최초 판매가 기준 35% 정률
     * - 정률 할인 15% -> 26,745원 할인
     * - 정률 할인 35% -> 62,405원 할인
     * - 프로모션 총 할인 89,150원
     * - 절삭 전 금액 : 178,300 - 89,150 = 89,150원
     * - 절삭 금액 : 150원
     * - 최종 할인 금액 (프로모션 총 할인 + 절삭 금액) = 89,150 + 150 = 89,300원
     * - 최종 금액 = 178,300 - 89,300 = 89,000원
     */
    @Test
    @DisplayName("[Product] - 할인 정책 정상 적용 테스트 2 - 다중 할인 [정률 + 정률]")
    void getValidProductAmount2() {
        // given - @BeforeEach createValidProductsAndPromotions
        // when
        ProductAmountResponse response = productService.getProductAmount(product2.getId());
        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(product2.getName()),
                () -> assertThat(response.getOriginPrice()).isEqualTo(product2.getPrice()),
                () -> assertThat(response.getDiscountPrice()).isEqualTo(89300),
                () -> assertThat(response.getFinalPrice()).isEqualTo(89000)
        );
    }

    /**
     * 정상 성공 TestCase 3
     * 최초 판매가격 : 353,990원
     * 할인 정책 : 최초 판매가 기준 35% 정률 단일 할인코드 적용
     * - 총 할인 35% -> 123,896원 할인
     * - 절삭 전 금액 : 353,900 - 123,896 = 230,004원
     * - 절삭 금액 : 4원
     * - 최종 할인 금액 (프로모션 총 할인 + 절삭 금액) = 123,896 + 4 = 123,900원
     * - 최종 금액 = 353,900 - 123,900 = 230,000원
     */
    @Test
    @DisplayName("[Product] - 할인 정책 정상 적용 테스트 3 - 단일 할인 [정률]")
    void getValidProductAmount3() {
        // given - @BeforeEach createValidProductsAndPromotions
        // when
        ProductAmountResponse response = productService.getProductAmount(product3.getId());
        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(product3.getName()),
                () -> assertThat(response.getOriginPrice()).isEqualTo(product3.getPrice()),
                () -> assertThat(response.getDiscountPrice()).isEqualTo(123990),
                () -> assertThat(response.getFinalPrice()).isEqualTo(230000)
        );
    }

    /**
     * 정상 성공 TestCase 4
     * 최초 판매가격 : 464,960원
     * 할인 정책 : 53,000원 정액 할인
     * - 절삭 전 금액 : 464,960 - 53,000 = 411,960원
     * - 절삭 금액 : 960원
     * - 최종 할인 금액 (프로모션 총 할인 + 절삭 금액) = 53,000 + 960 = 53,960원
     * - 최종 금액 = 464,960 - 53,960 = 411,000원
     */
    @Test
    @DisplayName("[Product] - 할인 정책 정상 적용 테스트 4 - 단일 할인 [정액]")
    void getValidProductAmount4() {
        // given - @BeforeEach createValidProductsAndPromotions
        // when
        ProductAmountResponse response = productService.getProductAmount(product4.getId());
        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(product4.getName()),
                () -> assertThat(response.getOriginPrice()).isEqualTo(product4.getPrice()),
                () -> assertThat(response.getDiscountPrice()).isEqualTo(53960),
                () -> assertThat(response.getFinalPrice()).isEqualTo(411000)
        );
    }

    /**
     * 정상 성공 TestCase 5
     * 최초 판매가격 : 720,060원
     * 할인 정책 : 30,000원 정액 할인 + 53,000원 정액 할인
     * - 절삭 전 금액 : 720,060 - 83,000 = 637,060원
     * - 절삭 금액 : 60원
     * - 최종 할인 금액 (프로모션 총 할인 + 절삭 금액) = 83,000 + 60 = 83,060원
     * - 최종 금액 = 720,060 - 83,060 = 637000원
     */
    @Test
    @DisplayName("[Product] - 할인 정책 정상 적용 테스트 5 - 다중 할인 [정액 + 정액]")
    void getValidProductAmount5() {
        // given - @BeforeEach createValidProductsAndPromotions
        // when
        ProductAmountResponse response = productService.getProductAmount(product5.getId());
        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(product5.getName()),
                () -> assertThat(response.getOriginPrice()).isEqualTo(product5.getPrice()),
                () -> assertThat(response.getDiscountPrice()).isEqualTo(83060),
                () -> assertThat(response.getFinalPrice()).isEqualTo(637000)
        );
    }

    /**
     * 예외 테스트 1
     * -2,147,483,648 이라는 존재하지 않는 쿠폰 아이디 매핑
     */
    @Test
    @DisplayName("[Product] - 할인 정책 예외 테스트 1 - 존재하지 않는 쿠폰 매핑시 예외를 던진다")
    void invalidPromotionCode() {
        // given - @BeforeEach create ValidProduct5
        promotionProductsRepository.save(
                PromotionProducts.of(-2147483648, product5.getId()));
        // when & then
        assertThatThrownBy(() -> productService.getProductAmount(product5.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(NOT_EXIST_COUPON.getMessage());
    }

    /**
     * 예외 테스트 2
     * -2,147,483,648이라는 존재하지 않는 상품 아이디 매핑
     */
    @Test
    @DisplayName("[Product] - 할인 정책 예외 테스트 2 - 존재하지 않는 상품 매핑시 예외를 던진다")
    void invalidProductCode() {
        // given
        promotionProductsRepository.save(
                PromotionProducts.of(1, -2147483648));
        // when & then
        assertThatThrownBy(() -> productService.getProductAmount(-2147483648))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(NOT_EXIST_PRODUCT.getMessage());
    }

    /**
     * 예외 테스트 3
     * 2023/03/31 까지 사용해야 하는, 유효하지 않은 쿠폰 매핑
     */
    @Test
    @DisplayName("[Product] - 할인 정책 예외 테스트 3 - 유효기간이 맞지 않는 쿠폰 매핑시 예외를 던진다 (만료된 쿠폰)")
    void invalidPromotionPeriod1() {
        Promotion invalidPromotion = promotionRepository.save(EXPIRED_COUPON.toEntity()); // 유효하지 않은 프로모션 -> 23/03/31 마감
        promotionProductsRepository.save(
                PromotionProducts.of(invalidPromotion.getId(), product6.getId()));
        // when & then
        assertThatThrownBy(() -> productService.getProductAmount(product6.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(INVALID_COUPON.getMessage());
    }

    /**
     * 예외 테스트 4
     * 2023/03/31 까지 사용해야 하는, 유효하지 않은 쿠폰 매핑
     */
    @Test
    @DisplayName("[Product] - 할인 정책 예외 테스트 4 - 유효기간이 맞지 않는 쿠폰 매핑시 예외를 던진다 (기간 이전 쿠폰)")
    void invalidPromotionPeriod2() {
        Promotion invalidPromotion = promotionRepository.save(BEFORE_USAGE_PERIOD_COUPON.toEntity()); // 유효하지 않은 프로모션 -> 23/03/31 마감
        promotionProductsRepository.save(
                PromotionProducts.of(invalidPromotion.getId(), product6.getId()));
        // when & then
        assertThatThrownBy(() -> productService.getProductAmount(product6.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(INVALID_COUPON.getMessage());
    }

    /**
     * 예외 테스트 5
     * "WON", "PERCENT"를 제외한 이상값이 있는 비정상 쿠폰 매핑
     */
    @Test
    @DisplayName("[Product] - 할인 정책 예외 테스트 5 - WON, PERCENT를 제외한 쿠폰 매핑시 예외를 던진다")
    void unknownPromotionCoupon() {
        Promotion invalidPromotion = promotionRepository.save(UNKNOWN_TYPE_COUPON.toEntity()); // 유효하지 않은 프로모션 -> 23/03/31 마감
        promotionProductsRepository.save(
                PromotionProducts.of(invalidPromotion.getId(), product6.getId()));
        // when & then
        assertThatThrownBy(() -> productService.getProductAmount(product6.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(INVALID_PROMOTION.getMessage());
    }

    /**
     * 예외 테스트 6
     * 최초 판매가격 : 45,000원
     * 할인 정책 : 53,000원 정액 할인
     * 최종 판매가가 10,000원 이하이므로 예외를 던진다.
     */
    @Test
    @DisplayName("[Product] - 할인 정책 예외 테스트 6 - 할인 적용 후 하한가 이하로 떨어지면 예외를 던진다")
    void belowLowerLimitPrice() {
        Promotion validPromotion = promotionRepository.save(VALID_PROMOTION4.toEntity()); // 유효하지 않은 프로모션 -> 23/03/31 마감
        promotionProductsRepository.save(
                PromotionProducts.of(validPromotion.getId(), product6.getId()));
        // when & then
        assertThatThrownBy(() -> productService.getProductAmount(product6.getId()))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(BELOW_LOWER_LIMIT.getMessage());
    }
}
