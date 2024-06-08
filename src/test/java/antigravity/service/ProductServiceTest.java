package antigravity.service;

import antigravity.domain.ProductWithPromotions;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.exception.NotSupportedPromotionException;
import antigravity.exception.ProductNotFoundException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class ProductServiceTest {

    @Autowired private ProductService service;

    @MockBean private ProductRepository productRepository;
    @MockBean private PromotionReader promotionReader;

    @Test
    @DisplayName("상품 id와 적용할 쿠폰 id 목록을 전달하여 상품 가격 구하기")
    public void getProductAmount() {
        // given
        Product product = anyProduct();

        ProductInfoRequest request = ProductInfoRequest.builder().productId(product.getId()).couponIds(new int[]{1,2}).build();
        given(productRepository.getProduct(anyInt())).willReturn(product);
        given(promotionReader.getAppliedPromotionOf(any())).willReturn(anyProductWithPromotions());

        // when
        ProductAmountResponse amount = service.getProductAmount(request);

        // then
        assertThat(amount).isNotNull();
        assertThat(amount.getName()).isEqualTo(product.getName());
        assertThat(amount.getOriginPrice()).isEqualTo(product.getPrice());
        assertThat(amount.getDiscountPrice()).isEqualTo(36000);
        assertThat(amount.getFinalPrice()).isEqualTo(179000);
    }

    @Test
    @DisplayName("상품 id가 없는 경우 ProductNotFoundException 예외 처리")
    public void getProductAmountNotFoundProduct() {
        // given
        Product product = anyProduct();

        ProductInfoRequest request = ProductInfoRequest.builder().productId(product.getId()).couponIds(new int[]{1,2}).build();
        given(productRepository.getProduct(anyInt())).willThrow(ProductNotFoundException.class);

        // when
        // then
        assertThrows(ProductNotFoundException.class, () -> service.getProductAmount(request));
    }

    @Test
    @DisplayName("해당 상품에 적용되지 않는 쿠폰 id를 전달할 경우 NotSupportedPromotionException 예외 발생")
    public void getProductAmountNotSupportedCouponId() {
        // given
        Product product = anyProduct();

        ProductInfoRequest request = ProductInfoRequest.builder().productId(product.getId()).couponIds(new int[]{1,3}).build();
        given(productRepository.getProduct(anyInt())).willReturn(product);
        given(promotionReader.getAppliedPromotionOf(any())).willReturn(anyProductWithPromotions());

        // when
        // then
        assertThrows(NotSupportedPromotionException.class, () -> service.getProductAmount(request));
    }

    private Product anyProduct() {
        return Product.builder().id(1).name("product1").price(215000).build();
    }

    private ProductWithPromotions anyProductWithPromotions() {
        return ProductWithPromotions.builder()
                .product(anyProduct())
                .promotions(List.of(
                        Promotion.builder().id(1).promotion_type("COUPON").discount_value(3000).build(),
                        Promotion.builder().id(2).promotion_type("CODE").discount_value(15).build()
                ))
                .build();
    }
}