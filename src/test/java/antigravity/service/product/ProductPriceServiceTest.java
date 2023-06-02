package antigravity.service.product;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.domain.PromotionProducts;
import antigravity.global.base.ServiceTestSupport;
import antigravity.dto.response.ProductAmountResponse;
import antigravity.service.promotion.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static antigravity.global.ProductFixture.*;
import static antigravity.global.PromotionFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("[SpringBootTest] ProductPriceService Test")
class ProductPriceServiceTest extends ServiceTestSupport {

    @Autowired
    private ProductService productService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private ProductPriceService productPriceService;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private Product product5;

    @BeforeEach
    void createValidProductsAndPromotions() {
        product1 = productService.createProduct(VALID_PRODUCT1.toEntity()); // 상품 1 -> 158,500 원
        product2 = productService.createProduct(VALID_PRODUCT2.toEntity()); // 상품 2 -> 178,300 원
        product3 = productService.createProduct(VALID_PRODUCT3.toEntity()); // 상품 3 -> 353,900 원
        product4 = productService.createProduct(VALID_PRODUCT4.toEntity()); // 상품 4 -> 464,960 원
        product5 = productService.createProduct(VALID_PRODUCT5.toEntity()); // 상품 5 -> 720,060 원
        Promotion promotion1 = promotionService.createPromotion(VALID_PROMOTION1.toEntity()); // 프로모션 1 -> 30,000원 정액 할인
        Promotion promotion2 = promotionService.createPromotion(VALID_PROMOTION2.toEntity()); // 프로모션 2 -> 15% 정률 할인
        Promotion promotion3 = promotionService.createPromotion(VALID_PROMOTION3.toEntity()); // 프로모션 3 -> 35% 정률 할인
        Promotion promotion4 = promotionService.createPromotion(VALID_PROMOTION4.toEntity()); // 프로모션 4 -> 53,000원 정액 할인
        // 상품 1 -> 프로모션 1 매핑
        productService.createProductPromotionMapping(
                PromotionProducts.of(promotion1.getId(), product1.getId()));
        // 상품 1 -> 프로모션 2 매핑
        productService.createProductPromotionMapping(
                PromotionProducts.of(promotion2.getId(), product1.getId()));
        // 상품 2 -> 프로모션 2 매핑
        productService.createProductPromotionMapping(
                PromotionProducts.of(promotion2.getId(), product2.getId()));
        // 상품 2 -> 프로모션 3 매핑
        productService.createProductPromotionMapping(
                PromotionProducts.of(promotion3.getId(), product2.getId()));
        // 상품 3 -> 프로모션 3 매핑
        productService.createProductPromotionMapping(
                PromotionProducts.of(promotion3.getId(), product3.getId()));
        // 상품 4 -> 프로모션 4 매핑
        productService.createProductPromotionMapping(
                PromotionProducts.of(promotion4.getId(), product4.getId()));
        // 상품 5 -> 프로모션 1 매핑
        productService.createProductPromotionMapping(
                PromotionProducts.of(promotion1.getId(), product5.getId()));
        // 상품 5 -> 프로모션 4 매핑
        productService.createProductPromotionMapping(
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
    @DisplayName("[ProductPrice] - 상품 가격 조회 서비스 정상 테스트 1 - 다중 할인 [정률 + 정액]")
    void getValidProductAmount1() {
        // given - @BeforeEach createValidProductsAndPromotions
        // when
        ProductAmountResponse response = productPriceService.getProductAmount(product1.getId());
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
    @DisplayName("[ProductPrice] - 상품 가격 조회 서비스 정상 테스트 2 - 다중 할인 [정률 + 정률]")
    void getValidProductAmount2() {
        // given - @BeforeEach createValidProductsAndPromotions
        // when
        ProductAmountResponse response = productPriceService.getProductAmount(product2.getId());
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
    @DisplayName("[ProductPrice] - 상품 가격 조회 서비스 정상 테스트 3 - 단일 할인 [정률]")
    void getValidProductAmount3() {
        // given - @BeforeEach createValidProductsAndPromotions
        // when
        ProductAmountResponse response = productPriceService.getProductAmount(product3.getId());
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
    @DisplayName("[ProductPrice] - 상품 가격 조회 서비스 정상 테스트 4 - 단일 할인 [정액]")
    void getValidProductAmount4() {
        // given - @BeforeEach createValidProductsAndPromotions
        // when
        ProductAmountResponse response = productPriceService.getProductAmount(product4.getId());
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
    @DisplayName("[ProductPrice] - 상품 가격 조회 서비스 정상 테스트 5 - 다중 할인 [정액 + 정액]")
    void getValidProductAmount5() {
        // given - @BeforeEach createValidProductsAndPromotions
        // when
        ProductAmountResponse response = productPriceService.getProductAmount(product5.getId());
        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(product5.getName()),
                () -> assertThat(response.getOriginPrice()).isEqualTo(product5.getPrice()),
                () -> assertThat(response.getDiscountPrice()).isEqualTo(83060),
                () -> assertThat(response.getFinalPrice()).isEqualTo(637000)
        );
    }












}
