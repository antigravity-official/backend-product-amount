package antigravity.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import antigravity.exception.ProductApplicationException;
import antigravity.exception.code.ProductErrorCode;
import antigravity.fixture.ProductFixture;
import antigravity.fixture.PromotionProductFixture;
import antigravity.model.request.ProductInfoRequest;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("- 상품관련 EXCEPTION 테스트 케이스")
public class ProductServiceExceptionTest {

    @Autowired private CalculatePriceService calculatePriceService;
    @MockBean private ProductRepository productRepository;
    @MockBean private PromotionProductsRepository promotionProductsRepository;

    @Test
    @DisplayName("[상품] 해당 상품이 존재하지 않는다면 ProductApplicationException(NOT_EXIST_PRODUCT)을 발생시킨다.")
    void productNotExistException() {
        given(productRepository.findById(1)).willReturn(Optional.empty());

        ProductApplicationException e = assertThrows(ProductApplicationException.class,
            () -> calculatePriceService.getProductAmount(mock(ProductInfoRequest.class)));

        assertEquals(ProductErrorCode.NOT_EXIST_PRODUCT, e.getErrorCode());
        then(productRepository).should().findById(any(Integer.class));
    }

    @Test
    @DisplayName("[상품] 해당 상품에 대한 입력값이 없다면 ProductApplicationException(NOT_EXIST_PRODUCT)을 발생시킨다.")
    void productRequestNotExistException() {
        ProductInfoRequest pir = ProductInfoRequest.builder().couponIds(new int[]{1}).build();

        ProductApplicationException e = assertThrows(ProductApplicationException.class,
            () -> calculatePriceService.getProductAmount(pir));

        assertEquals(ProductErrorCode.NOT_EXIST_PRODUCT, e.getErrorCode());
    }

    @Test
    @DisplayName("[상품] 해당 프로모션을 적용한 상품 가격이 주어진 제한 가격보다 작다면 ProductApplicationException(MAX_PRICE_LIMIT)을 발생시킨다.")
    void productPriceLimitMinException() {
        ProductInfoRequest pir = ProductInfoRequest.builder().productId(1).couponIds(new int[]{6}).build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(ProductFixture.getProduct()));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(any())).willReturn(
            Collections.singletonList(PromotionProductFixture.getMinPricePromotionProducts()));

        ProductApplicationException e = assertThrows(ProductApplicationException.class,
            () -> calculatePriceService.getProductAmount(pir));

        assertEquals(ProductErrorCode.MIN_PRICE_LIMIT, e.getErrorCode());
        then(productRepository).should().findById(any(Integer.class));
    }

    @Test
    @DisplayName("[상품] 해당 프로모션을 적용한 상품 가격이 주어진 제한 가격보다 초과된다면 ProductApplicationException(MAX_PRICE_LIMIT)을 발생시킨다.")
    void productPriceLimitMaxException() {
        ProductInfoRequest pir = ProductInfoRequest.builder().productId(1).couponIds(new int[]{2}).build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(ProductFixture.getMaxProduct()));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(any())).willReturn(
            Collections.singletonList(PromotionProductFixture.getMaxPricePromotionProducts()));

        ProductApplicationException e = assertThrows(ProductApplicationException.class,
            () -> calculatePriceService.getProductAmount(pir));

        assertEquals(ProductErrorCode.MAX_PRICE_LIMIT, e.getErrorCode());
    }

}
