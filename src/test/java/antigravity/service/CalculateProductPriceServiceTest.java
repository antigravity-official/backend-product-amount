package antigravity.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import antigravity.exception.ProductApplicationException;
import antigravity.exception.code.ProductErrorCode;
import antigravity.exception.code.PromotionErrorCode;
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
class CalculateProductPriceServiceTest {

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
    @DisplayName("중복되는 프로모션 적용이 요청된다면 ProductApplicationException(DUPLICATED_PROMOTION)을 발생시킨다.")
    void promotionDuplicationException() {
        ProductInfoRequest pir = ProductInfoRequest.builder().productId(1).couponIds(new int[]{1, 1}).build();
        given(productRepository.findById(1)).willReturn(Optional.ofNullable(ProductFixture.getProduct()));

        ProductApplicationException e = assertThrows(ProductApplicationException.class,
            () -> calculateProductPriceService.getProductAmount(pir));

        assertEquals(PromotionErrorCode.DUPLICATED_PROMOTION, e.getErrorCode());
        then(productRepository).should().findById(any(Integer.class));
    }

    @Test
    @DisplayName("해당 프로모션의 기한이 지났다면 ProductApplicationException(INVALID_PROMOTION_PERIOD)을 발생시킨다.")
    void promotionPeriodInvalidException() {
        ProductInfoRequest pir = ProductInfoRequest.builder().productId(1).couponIds(new int[]{1, 2}).build();
        given(productRepository.findById(1)).willReturn(Optional.ofNullable(ProductFixture.getProduct()));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(Arrays.asList(1, 2))).willReturn(
            Collections.singletonList(PromotionProductFixture.getExpiredPeriodPromotionProducts()));

        ProductApplicationException e = assertThrows(ProductApplicationException.class,
            () -> calculateProductPriceService.getProductAmount(pir));

        assertEquals(PromotionErrorCode.INVALID_PROMOTION_PERIOD, e.getErrorCode());
    }

    @Test
    @DisplayName("해당 프로모션의에 해당되는 상품이 아니라면 ProductApplicationException(INVALID_PROMOTION_PRODUCT)을 발생시킨다.")
    void promotionOfProductInvalidException() {
        ProductInfoRequest pir = ProductInfoRequest.builder().productId(1).couponIds(new int[]{1, 2}).build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(ProductFixture.getProduct()));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(Arrays.asList(1, 2))).willReturn(
            Collections.singletonList(PromotionProductFixture.getInValidPromotionProducts()));

        ProductApplicationException e = assertThrows(ProductApplicationException.class,
            () -> calculateProductPriceService.getProductAmount(pir));

        assertEquals(PromotionErrorCode.INVALID_PROMOTION_PRODUCT, e.getErrorCode());
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

    @Test
    @DisplayName("해당 프로모션 할인가격이 기존 제품 가격보다 초과된다면 ProductApplicationException(EXCEED_ORIGIN_PRICE)을 발생시킨다.")
    void promotionPriceExceedProductPriceException() {
        ProductInfoRequest pir = ProductInfoRequest.builder().productId(1).couponIds(new int[]{1, 2}).build();

        given(productRepository.findById(1)).willReturn(Optional.ofNullable(ProductFixture.getProduct()));
        given(promotionProductsRepository.findWithPromotionByPromotionIdIn(Arrays.asList(1, 2))).willReturn(
            Collections.singletonList(PromotionProductFixture.getExceedPromotionPriceProducts()));

        ProductApplicationException e = assertThrows(ProductApplicationException.class,
            () -> calculateProductPriceService.getProductAmount(pir));

        assertEquals(PromotionErrorCode.EXCEED_ORIGIN_PRICE, e.getErrorCode());
    }

}