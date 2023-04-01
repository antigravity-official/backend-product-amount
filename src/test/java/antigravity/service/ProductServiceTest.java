package antigravity.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import antigravity.model.request.ProductInfoRequest;

@SpringBootTest
class ProductServiceTest {
	
	@Autowired
	private ProductService productService;

    @Test
    @DisplayName("상품 가격 추출 성공 - 쿠폰 + 코드 프로모션 할인")
    void getProductAmount() {
        System.out.println("상품 가격 추출 테스트");
        //given
        int productId = 1;
        int[] couponIds = {1, 2};
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(productId)
                .couponIds(couponIds).build();

        //when
        Throwable exception = assertThrows(Exception.class, () -> productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("프로모션 중 적용가능한 것이 존재하지 않습니다.");
    }
    
    
    @Test
    @DisplayName("상품 가격 추출 실패 - 상품 아이디 조회 불가")
    void getProductAmountFailNotExistsProduct() {
        //given
        int productId = 5;
        int[] couponIds = {1, 2};
        ProductInfoRequest request = ProductInfoRequest.builder()
                .productId(productId)
                .couponIds(couponIds).build();

        //when
        Throwable exception = assertThrows(Exception.class, () -> productService.getProductAmount(request));

        //then
        assertThat(exception.getMessage()).isEqualTo("상품이 존재하지 않습니다.");
    }
    
}