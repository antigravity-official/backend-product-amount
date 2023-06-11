package antigravity.service;

import antigravity.exception.CommonException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("성공 케이스")
    void success() throws Exception {

        // given
        int[] couponIds = {1, 2};

        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(couponIds)
                .build();

        // when
        ProductAmountResponse response = productService.getProductAmount(request);

        // then
        Assertions.assertThat(response.getFinalPrice()).isEqualTo(185000);
    }

    @Test
    @DisplayName("예외 케이스 - productId")
    void notFoundProductId() throws Exception {

        // given
        int[] couponIds = {1, 2};

        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(10)
                .couponIds(couponIds)
                .build();

        // when

        // then
        Throwable exception = assertThrows(CommonException.class, () -> {
            productService.getProductAmount(request);
        });

        String message = exception.getMessage();
        Assertions.assertThat("해당 id의 PRODUCT 를 찾을 수 없습니다").isEqualTo(message);

    }


}