package antigravity.service.product;

import antigravity.domain.Product;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static antigravity.error.ErrorCode.NOT_EXIST_PRODUCT;
import static antigravity.global.ProductFixture.VALID_PRODUCT1;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 *  ProductService
 *  Service Layer - Mock Test
 */
@DisplayName("[Service] ProductService - Service Layer Mock Test")
class ProductServiceTest extends ServiceTestSupport {

    @InjectMocks
    private ProductService productService;

    private static final int PRODUCT_ID = 1;

    Product product = VALID_PRODUCT1.toEntity();


    @Nested
    @DisplayName("[findProductByID] 상품 검색 비즈니스 로직")
    class TestFindProductById {

        @Test
        @DisplayName("[Success] ProductId를 기준으로, DB에서 해당 Product를 정상적으로 찾아 리턴한다.")
        void SuccessToFindProductById() throws Exception {
            //given
            when(productQueryRepo.findById(PRODUCT_ID)).thenReturn(ofNullable(product));

            //when
            Product foundProduct = productService.findProductById(PRODUCT_ID);

            //then
            verify(productQueryRepo).findById(PRODUCT_ID);
            assertNotNull(foundProduct);
        }
    }
}
