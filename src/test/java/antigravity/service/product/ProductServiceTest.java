package antigravity.service.product;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.domain.PromotionProducts;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import antigravity.repository.product.ProductRepository;
import antigravity.service.promotion.PromotionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static antigravity.error.ErrorCode.NOT_EXIST_PRODUCT;
import static antigravity.global.ProductFixture.LOW_PRICE_PRODUCT;
import static antigravity.global.PromotionFixture.VALID_PROMOTION4;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("[SpringBootTest] ProductService Test")
class ProductServiceTest extends ServiceTestSupport {

    @Autowired
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = productService.createProduct(LOW_PRICE_PRODUCT.toEntity()); // 상품 -> 45,000 원
    }

    /**
     * 예외 테스트 1
     * -2,147,483,648이라는 존재하지 않는 상품 아이디 매핑
     */
    @Test
    @DisplayName("[Product] - 상품 서비스 예외 테스트 1 - 존재하지 않는 상품 매핑시 예외를 던진다")
    void invalidProductCode() {
        // given & when & then
        assertThatThrownBy(() -> productService.findProductByProductId(-2147483648))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(NOT_EXIST_PRODUCT.getMessage());
    }



    @Test
    @DisplayName("[Product] - 상품 서비스 정상 테스트 1 - 상품 정보를 정상적으로 저장한다")
    void createValidProduct() {
        // given - @BeforeEach setUp
        // when
        Product savedProduct = productQueryRepository.findById(product.getId()).get();

        // then
        assertThat(product).isEqualTo(savedProduct);
    }

    @Test
    @DisplayName("[Product] - 상품 서비스 정상 테스트 2 - 상품 Id로 상품을 조회한다")
    void findValidProduct() {
        // given - @BeforeEach setUp
        // when
        Product savedProduct = productService.findProductByProductId(product.getId());

        //then
        assertThat(product).isEqualTo(savedProduct);
    }

    @Test
    @DisplayName("[Product] - 상품 서비스 정상 테스트 3 - 상품에 해당하는 쿠폰 매핑 정보를 정상적으로 저장한다")
    void createValidPromotionProductions() {
        // given - @BeforeEach setUp
        // when
        Product savedProduct = productQueryRepository.findById(product.getId()).get();

        //then
        assertThat(product).isEqualTo(savedProduct);
    }
}
