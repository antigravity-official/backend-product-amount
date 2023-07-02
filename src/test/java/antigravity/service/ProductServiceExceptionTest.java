package antigravity.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import antigravity.exception.ProductApplicationException;
import antigravity.exception.code.ProductErrorCode;
import antigravity.fixture.ProductFixture;
import antigravity.fixture.PromotionProductFixture;
import antigravity.model.request.ProductInfoRequest;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ProductServiceExceptionTest {

    @Autowired private CalculateProductPriceService calculateProductPriceService;
    @MockBean private ProductRepository productRepository;
    @MockBean private PromotionProductsRepository promotionProductsRepository;

    @Test
    @DisplayName("해당 상품이 존재하지 않는다면 ProductApplicationException(NOT_EXIST_PRODUCT)을 발생시킨다.")
    void productNotExistException() {
        ProductInfoRequest pir = ProductInfoRequest.builder().productId(1).couponIds(new int[]{1, 2}).build();
        given(productRepository.findById(1)).willReturn(Optional.empty());

        ProductApplicationException e = assertThrows(ProductApplicationException.class,
            () -> calculateProductPriceService.getProductAmount(pir));

        assertEquals(ProductErrorCode.NOT_EXIST_PRODUCT, e.getErrorCode());
        then(productRepository).should().findById(any(Integer.class));
    }

    @Test
    @DisplayName("해당 프로모션을 적용한 상품 가격이 주어진 제한 가격보다 작다면 ProductApplicationException(MAX_PRICE_LIMIT)을 발생시킨다.")
    void productPriceLimitMinException() {
        ProductInfoRequest pir = ProductInfoRequest.builder().productId(1).couponIds(new int[]{1, 2}).build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(ProductFixture.getProduct()));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(Arrays.asList(1, 2))).willReturn(
            Collections.singletonList(PromotionProductFixture.getMinPricePromotionProducts()));

        ProductApplicationException e = assertThrows(ProductApplicationException.class,
            () -> calculateProductPriceService.getProductAmount(pir));

        assertEquals(ProductErrorCode.MIN_PRICE_LIMIT, e.getErrorCode());
    }

    @Test
    @DisplayName("해당 프로모션을 적용한 상품 가격이 주어진 제한 가격보다 초과된다면 ProductApplicationException(MAX_PRICE_LIMIT)을 발생시킨다.")
    void productPriceLimitMaxException() {
        ProductInfoRequest pir = ProductInfoRequest.builder().productId(1).couponIds(new int[]{1, 2}).build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(ProductFixture.getMaxProduct()));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(Arrays.asList(1, 2))).willReturn(
            Collections.singletonList(PromotionProductFixture.getMaxPricePromotionProducts()));

        ProductApplicationException e = assertThrows(ProductApplicationException.class,
            () -> calculateProductPriceService.getProductAmount(pir));

        assertEquals(ProductErrorCode.MAX_PRICE_LIMIT, e.getErrorCode());
    }

}
