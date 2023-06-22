package antigravity.repository;

import annotation.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("상품과 매핑된 프로모션 조회 시 성공")
    void When_getProductWithPromotions_Then_Success () {
        // given
        int productId = 1;
        int[] promotionIds = {1,2};
        // when
        List<PromotionProductsDto> promotionProductsDtoList = productRepository.getProductWithPromotions(productId, promotionIds);
        // then
        assertEquals(2, promotionProductsDtoList.size());
    }

    @Test
    @DisplayName("상품과 매핑된 프로모션이 없을 경우 빈 값 리턴")
    void When_WithoutProductWithPromotions_Then_ReturnEmpty () throws Exception {
        // given
        int productId = 1;
        int[] promotionIds = {3};
        // when
        List<PromotionProductsDto> promotionProductsDtoList = productRepository.getProductWithPromotions(productId, promotionIds);
        // then
        assertEquals(0, promotionProductsDtoList.size());
    }
}