package antigravity.service.promotion;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.domain.PromotionProducts;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import antigravity.service.discount.DiscountPolicy;
import antigravity.service.discount.DiscountPolicyFactory;
import antigravity.service.discount.FixDiscountPolicy;
import antigravity.service.product.ProductPriceService;
import antigravity.service.product.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static antigravity.error.ErrorCode.*;
import static antigravity.global.ProductFixture.*;
import static antigravity.global.PromotionFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("[SpringBootTest] PromotionService Test")
class PromotionServiceTest extends ServiceTestSupport {

    @Autowired
    private ProductService productService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private ProductPriceService productPriceService;

    private Product product5;
    private Product product6;
    private List<Promotion> invalidPromotions = new ArrayList<>();

    @BeforeEach
    void createValidProductsAndPromotions() {
        product5 = productService.createProduct(VALID_PRODUCT5.toEntity()); // 상품 5 -> 720,060 원
        product6 = productService.createProduct(LOW_PRICE_PRODUCT.toEntity()); // 상품 6 -> 45,000 원
        Promotion promotion1 = promotionService.createPromotion(VALID_PROMOTION1.toEntity()); // 프로모션 1 -> 30,000원 정액 할인
        Promotion promotion4 = promotionService.createPromotion(VALID_PROMOTION4.toEntity()); // 프로모션 4 -> 53,000원 정액 할인
        Promotion invalidPromotion1 = promotionService.createPromotion(BEFORE_USAGE_PERIOD_COUPON.toEntity()); // 프로모션 4 -> 53,000원 정액 할인
        Promotion invalidPromotion2 = promotionService.createPromotion(EXPIRED_COUPON.toEntity()); // 프로모션 4 -> 53,000원 정액 할인
        invalidPromotions.add(invalidPromotion1);
        invalidPromotions.add(invalidPromotion2);
        // 상품 5 -> 프로모션 1 매핑
        productService.createProductPromotionMapping(
                PromotionProducts.of(promotion1.getId(), product5.getId()));
        // 상품 5 -> 프로모션 4 매핑
        productService.createProductPromotionMapping(
                PromotionProducts.of(promotion4.getId(), product5.getId()));
    }

    /**
     * 예외 테스트 1
     * -2,147,483,648 이라는 존재하지 않는 쿠폰 아이디 매핑
     */
    @Test
    @DisplayName("[Promotion] - 프로모션 서비스 예외 테스트 1 - 매핑 테이블에서 유효하지 않은 프로모션 아이디가 매핑될 경우 예외를 던진다")
    void notExistPromotionCoupon() {
        //given - @BeforeEach setUp
        //when & then
        assertThatThrownBy(() -> promotionService.findValidatePromotionsByIds(new int[]{-30, -50}))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(NOT_EXIST_PROMOTION.getMessage());
    }


    /**
     * 예외 테스트 2
     * 2023/03/31 까지 사용해야 하는, 유효하지 않은 쿠폰 매핑
     * 2055/09/01 부터 사용이 가능한, 유효하지 않은 쿠폰 매핑
     */
    @Test
    @DisplayName("[Promotion] - 프로모션 서비스 예외 테스트 2 - 유효기간이 맞지 않는 프로모션 매핑시 예외를 던진다")
    void invalidPromotionPeriod() {
        // given - @BeforeEach create ValidProduct5
        // when & then
        assertThatThrownBy(() -> promotionService.validatePromotions(invalidPromotions))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(INVALID_PROMOTION_PERIOD.getMessage());
    }
}
