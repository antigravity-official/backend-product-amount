package antigravity.service.product;

import antigravity.domain.Product;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static antigravity.error.ErrorCode.NOT_EXIST_PRODUCT;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

// todo 테스트코드 작성 끝~

/**
 *  ProductService
 *  Service Layer - Mock Test
 */
@DisplayName("[Service] ProductService - Service Layer Mock Test")
class ProductServiceTest extends ServiceTestSupport {

    @InjectMocks
    private ProductService productService;

    private final int PRODUCT_ID = 1;
    Product product = mock(Product.class);

    @Nested
    @DisplayName("[findProductByID] 상품 검색 비즈니스 로직")
    class TestFindProductById {

        @Test
        @DisplayName("[Success] 상품 아이디를 기준으로, DB에서 해당 Product를 정상적으로 찾아 리턴한다.")
        void When_ExistenceProductRequested_Expect_ReturnProduct() throws Exception {
            //given
            when(productQueryRepo.findById(PRODUCT_ID)).thenReturn(ofNullable(product));

            //when
            Product foundProduct = productService.findProductById(PRODUCT_ID);

            //then
            verify(productQueryRepo, times(1)).findById(PRODUCT_ID);
            assertNotNull(foundProduct);
        }

        @Test
        @DisplayName("[Exception] DB에 해당 상품 아이디가 존재하지 않을 경우 예외를 던진다.")
        void When_NonExistenceProductRequested_Expect_ThrowException() throws Exception {
            //given
            when(productQueryRepo.findById(PRODUCT_ID)).thenThrow(new BusinessException(NOT_EXIST_PRODUCT));

            //when && then
            assertThatThrownBy(() -> productService.findProductById(PRODUCT_ID))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(NOT_EXIST_PRODUCT.getMessage());
        }
    }
}
