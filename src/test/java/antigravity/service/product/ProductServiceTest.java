package antigravity.service.product;

import antigravity.domain.product.ProductRepository;
import antigravity.domain.promotion.DiscountType;
import antigravity.domain.promotion.PromotionType;
import antigravity.service.product.dto.GetProductAmountDto;
import antigravity.service.product.exception.ProductNotFoundException;
import antigravity.service.product.resource.GetProductAmountResource;
import antigravity.service.promotion.PromotionService;
import antigravity.service.promotion.dto.GetAvailablePromotionsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static antigravity.domain.product.ProductFixture.aProduct;
import static antigravity.domain.promotion.PromotionFixture.aPromotion;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class ProductServiceTest {
    ProductService productService;
    ProductRepository productRepository;
    PromotionService promotionService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        promotionService = mock(PromotionService.class);
        productService = new ProductService(productRepository, promotionService, new BasicFinalProductAmountCalculator());
    }

    @Test
    @DisplayName("해당 상품이 존재하지 않는다면 ProductNotFoundException을 발생시킨다.")
    void getProductAmount_1() {
        // given
        when(productRepository.findById(1L))
                .thenReturn(Optional.empty());

        // when
        assertThrows(ProductNotFoundException.class, () -> {
            GetProductAmountDto getProductAmountDto = new GetProductAmountDto(1L, asList(1L, 2L), LocalDateTime.now());
            productService.getProductAmount(getProductAmountDto);
        });
    }

    @Test
    @DisplayName(
        "101,000원 상품에 적용 가능한 프로모션이 존재하지 않을 경우 " +
        "최종 금액은 101,000원이고 할인 금액은 0원이다."
    )
    void getProductAmount_2() {
        // given
        LocalDateTime now = LocalDateTime.now();
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(aProduct()
                        .price(101_000)
                        .build()));
        GetAvailablePromotionsDto getAvailablePromotionsDto = new GetAvailablePromotionsDto(
                1L,
                asList(1L, 2L),
                now
        );
        when(promotionService.getAvailablePromotions(getAvailablePromotionsDto))
                .thenReturn(asList(
                ));

        // when
        GetProductAmountDto getProductAmountDto = new GetProductAmountDto(1L, asList(1L, 2L), now);
        GetProductAmountResource resource = productService.getProductAmount(getProductAmountDto);

        // then
        assertEquals(0, resource.getDiscountPrice());
        assertEquals(101_000, resource.getFinalPrice());
    }

    @Test
    @DisplayName(
        "200,000원 상품에 100,000원 정액 프로모션과 20% 정률 프로모션을 적용할 수 있을 경우 " +
        "최종 금액은 80,000원이고 할인 금액은 120,000원이다."
    )
    void getProductAmount_3() {
        // given
        LocalDateTime now = LocalDateTime.now();
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(aProduct()
                        .price(200_000)
                        .build()));
        GetAvailablePromotionsDto getAvailablePromotionsDto = new GetAvailablePromotionsDto(
                1L,
                asList(1L, 2L),
                now
        );
        when(promotionService.getAvailablePromotions(getAvailablePromotionsDto))
                .thenReturn(asList(
                        aPromotion()
                                .promotionType(PromotionType.COUPON)
                                .discountType(DiscountType.WON)
                                .discountValue(100_000)
                                .build(),
                        aPromotion()
                                .promotionType(PromotionType.COUPON)
                                .discountType(DiscountType.PERCENT)
                                .discountValue(20)
                                .build()
                ));

        // when
        GetProductAmountDto getProductAmountDto = new GetProductAmountDto(1L, asList(1L, 2L), now);
        GetProductAmountResource resource = productService.getProductAmount(getProductAmountDto);

        // then
        assertEquals(120_000, resource.getDiscountPrice());
        assertEquals(80_000, resource.getFinalPrice());
    }
}