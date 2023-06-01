package antigravity.service;

import antigravity.domain.entity.Product;
import antigravity.global.base.ServiceTestSupport;
import antigravity.model.response.ProductAmountResponse;
import antigravity.service.discount.DiscountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static antigravity.global.ProductFixture.VALID_PRODUCT1;
import static antigravity.global.PromotionFixture.VALID_PROMOTION1;
import static antigravity.global.PromotionFixture.VALID_PROMOTION2;


class DiscountServiceTest extends ServiceTestSupport {

    @Autowired
    private DiscountService discountService;

    @Autowired
    private ProductService productService;

    private Product product;

    @BeforeEach
    void createValidProductsAndPromotions(){
        product = productRepository.save(VALID_PRODUCT1.toEntity());
        promotionRepository.save(VALID_PROMOTION1.toEntity());
        promotionRepository.save(VALID_PROMOTION2.toEntity());
    }

    /**
     *  TestCase 1
     *  최초 판매가격 : 158,500원
     *  할인 정책 : 최초 판매가 기준 30% 정률 + 30,000원 정액 할인
     *  - 정률 할인 15% -> 23,775원 할인
     *  - 정액 할인 30,000원
     *  - 프로모션 총 할인 53,775원
     *  - 절삭 전 금액 : 158,500 - 53,775 = 104,725원
     *  - 절삭 금액 : 725원
     *  - 최종 할인 금액 (프로모션 총 할인 + 절삭 금액) = 53,775 + 725 = 54,500원
     *  - 최종 금액 = 158,500 - 54,500 = 104,000원
     */
    @Test
    @DisplayName("[Product] - 할인 정책을 정상적으로 적용해 최종 금액을 리턴")
    void getValidProductAmount() {
        //given - @BeforeEach createValidProductsAndPromotions
        //when
        ProductAmountResponse productAmount = productService.getProductAmount(product.getId());
        //then
    }
}
