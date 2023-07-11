package antigravity.service.product;

import antigravity.domain.Product;
import antigravity.domain.PromotionProducts;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import static antigravity.domain.PromotionProducts.of;
import static antigravity.error.ErrorCode.NOT_EXIST_PRODUCT;
import static antigravity.global.ProductFixture.VALID_PRODUCT1;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Disabled
@DisplayName("[Service] ProductService - SpringBootTest")
public class ProductServiceTest extends ServiceTestSupport {

    @Autowired
    private ProductService productService;

    Product product;
    PromotionProducts mapping;

    @BeforeEach
    void initSetting() {
        product = VALID_PRODUCT1.toEntity();
        mapping = of(1, 1);
    }

    @Nested
    @DisplayName("[findProductByID] 상품 검색 비즈니스 로직")
    class findProductByIdTest {

        @Test
        @DisplayName("[Success] ProductId를 기준으로, DB에서 해당 Product를 정상적으로 찾아 리턴한다.")
        void SuccessToFindProductById() throws Exception {
            //given
            given(productQueryRepo.findById(any())).willReturn(ofNullable(product));
            //when
            Product foundProduct = productService.findProductById(product.getId());
            //then
            assertThat(product).isSameAs(foundProduct);
        }

        @Test
        @DisplayName("[Exception] 해당 ProductId가 DB에 존재하지 않을 때, 예외를 던진다.")
        void failByNonexistentProduct() throws Exception {
            //given - @BeforeEach
            given(productQueryRepo.findById(any())).willReturn(empty());
            String expectedErrorMessage = NOT_EXIST_PRODUCT.getMessage();
            //when && then
            assertThatThrownBy(() -> productService.findProductById(product.getId()))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(expectedErrorMessage);
        }
    }
}
