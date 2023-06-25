package antigravity.service;

import antigravity.model.request.ProductInfoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    void getProductAmount() {
        System.out.println("상품 가격 추출 테스트");
    }

    @Test
    void select() {
        int[] couponIds = {1, 2};

        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(1)
                .couponIds(couponIds)
                .build();

        productService.getProductAmount(request);
    }
}