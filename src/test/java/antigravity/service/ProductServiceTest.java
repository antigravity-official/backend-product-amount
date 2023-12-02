package antigravity.service;

import static antigravity.constants.DiscountType.*;
import static antigravity.constants.PromotionType.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import antigravity.constants.DiscountType;
import antigravity.constants.PromotionType;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.exception.InvalidDateException;
import antigravity.exception.NotAllowedAmountRangeException;
import antigravity.exception.NotAllowedDiscountTypeException;
import antigravity.exception.NotFoundProductException;
import antigravity.exception.NotFoundPromotionException;
import antigravity.exception.NotFoundPromotionProductsException;
import antigravity.model.request.ProductInfoRequest;
import antigravity.model.response.ProductAmountResponse;
import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import antigravity.repository.PromotionRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PromotionProductsRepository promotionProductsRepository;

    @InjectMocks
    private ProductService productService;

    private ProductInfoRequest productInfoRequest;
    private Product product;
    private List<PromotionProducts> promotionProducts;

    private static final LocalDateTime validStartDate = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime validEndDate = LocalDateTime.now().plusDays(1);

    @BeforeEach
    void setUp() {
        productInfoRequest = ProductInfoRequest.builder()
            .productId(1)
            .couponIds(new int[] {1, 2})
            .build();

        product = Product.builder()
            .name("상품1")
            .id(1)
            .price(215000)
            .build();

        promotionProducts = Arrays.asList(
            new PromotionProducts(1, getCouponPromotion(validStartDate, validEndDate), product),
            new PromotionProducts(2, getCodePromotion(validStartDate, validEndDate), product)
        );

    }

    @Test
    @DisplayName("해당 제품의 할인 정보가 유효할 때 결제 정보를 Response 객체로 반환한다.")
    void when_validDiscountInfo_expect_returnPaymentInfoResponse() {
        int originPrice = product.getPrice();
        List<Integer> couponIdsList = Arrays
            .stream(productInfoRequest.getCouponIds())
            .boxed()
            .toList();

        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(promotionProductsRepository.findPromotionProductsByPromotionIdIn(couponIdsList))
            .willReturn(promotionProducts);

        productService.validateAmountRange(originPrice);

        given(promotionRepository.findById(1))
            .willReturn(Optional.of(getCouponPromotion(validStartDate, validEndDate)));
        given(promotionRepository.findById(2))
            .willReturn(Optional.of(getCodePromotion(validStartDate, validEndDate)));

        productService.validateDate(promotionProducts);

        int discountPrice = productService.calculateDiscountPrice(promotionProducts, originPrice);
        int finalPrice = productService.calculateFinalPrice(originPrice, discountPrice);
        ProductAmountResponse result = productService.getProductAmount(productInfoRequest);

        assertNotNull(result);
        assertAll(
            () -> assertThat(result.getOriginPrice()).isEqualTo(originPrice),
            () -> assertThat(result.getDiscountPrice()).isEqualTo(discountPrice),
            () -> assertThat(result.getFinalPrice()).isEqualTo(finalPrice)
        );
    }

    @Test
    @DisplayName("해당 정보에 해당하는 프로덕트가 존재하지 않으면 NotFoundProductException이 발생한다.")
    void when_productDoesNotExists_then_throwsNotFoundProductException() {
        int notExistProductId = 101;

        given(productRepository.findById(anyInt())).willThrow(NotFoundProductException.class);

        assertThatThrownBy(() ->
            productService.getProductById(notExistProductId))
            .isInstanceOf(NotFoundProductException.class);
    }

    @Test
    @DisplayName("해당 정보에 해당하는 매칭 정보가 존재하지 않으면 NotFoundPromotionProductsException이 발생한다.")
    void when_productProductsDoesNotExists_then_throwsNotFoundPromotionProductsException() {
        List<Integer> notExistMatchingIds = Arrays
            .stream(new int[] {3, 4})
            .boxed()
            .toList();

        assertThatThrownBy(() ->
            productService.getPromotionProducts(notExistMatchingIds))
            .isInstanceOf(NotFoundPromotionProductsException.class);
    }

    @Test
    @DisplayName("해당 정보에 해당하는 프로모션이 존재하지 않으면 NotFoundPromotionException이 발생한다.")
    void when_productDoesNotExists_then_throwsNotFoundPromotionException() {
        int notExistPromotionId = 101;

        assertThatThrownBy(() ->
            productService.getPromotionById(notExistPromotionId))
            .isInstanceOf(NotFoundPromotionException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {9000, 11000000})
    @DisplayName("상품 가격 범위를 벗어났다면 NotFoundPromotionException이 발생한다.")
    void when_amountRangeIsInvalid_then_throwsNotAllowedAmountRangeException(int invalidPrice) {
        assertThatThrownBy(() ->
            productService.validateAmountRange(invalidPrice))
            .isInstanceOf(NotAllowedAmountRangeException.class);
    }

    @Nested
    @DisplayName("할인 유효 기간 검증")
    class DiscountAvailableTest {
        @Test
        @DisplayName("현재 시각이 할인 적용 가능 시간 전이라면 InvalidDateException이 발생한다.")
        void when_startDateIsAfterNow_then_throwsInvalidDateException() {
            LocalDateTime invalidStartTime = LocalDateTime.now().plusDays(1);

            Promotion promotion = getCodePromotion(invalidStartTime, validEndDate);

            given(promotionRepository.findById(anyInt())).willReturn(Optional.of(promotion));

            List<PromotionProducts> promotionProducts = Arrays.asList(
                new PromotionProducts(1, promotion, product)
            );

            assertThatThrownBy(() ->
                productService.validateDate(promotionProducts))
                .isInstanceOf(InvalidDateException.class);
        }

        @Test
        @DisplayName("현재 시각이 할인 적용 가능 시간 후라면 InvalidDateException이 발생한다.")
        void when_endDateIsBeforeNow_then_throwsInvalidDateException() {
            LocalDateTime invalidEndTime = LocalDateTime.now().minusDays(1);

            Promotion promotion = getCodePromotion(validStartDate, invalidEndTime);

            given(promotionRepository.findById(anyInt())).willReturn(Optional.of(promotion));

            List<PromotionProducts> promotionProducts = Arrays.asList(
                new PromotionProducts(1, promotion, product)
            );

            assertThatThrownBy(() ->
                productService.validateDate(promotionProducts))
                .isInstanceOf(InvalidDateException.class);
        }
    }

    @Test
    @DisplayName("유효한 프로모션 정보가 존재할 때 할인 가격을 반환한다.")
    void when_promotionInformationIsValid_then_returnDiscountPrice() {
        int originPrice = product.getPrice();
        int expectedPrice = 62250;

        int discountPrice =
            productService.calculateDiscountPrice(promotionProducts, originPrice);

        assertThat(discountPrice).isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("유효하지 않은 Discount Type이 존재할 때 NotAllowedDiscountTypeException이 발생한다.")
    void when_discountTypeIsInvalid_then_throwsNotAllowedDiscountTypeException() {
        Promotion promotionWithInvalidType = Promotion.builder()
            .discount_type(INVALID_TYPE)
            .build();

        List<PromotionProducts> promotionProducts = Arrays.asList(
            new PromotionProducts(1, promotionWithInvalidType, product));

        assertThatThrownBy(() ->
            productService.calculateDiscountPrice(promotionProducts, product.getPrice()))
            .isInstanceOf(NotAllowedDiscountTypeException.class);
    }

    @Test
    @DisplayName("유효한 가격 정보가 주어지면 최종 가격을 반환한다.")
    void when_priceInformationIsValid_then_returnFinalPrice() {
        int discountPrice = 62250;
        int expectedPrice = 152000;

        int finalPrice = productService.calculateFinalPrice(product.getPrice(), discountPrice);

        assertThat(finalPrice).isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("계산된 최종 가격이 음수일 때 0을 반환한다.")
    void when_finalPriceIsNegative_then_returnZero() {
        int originPrice = 5000;
        int discountPrice = 10000;

        int finalPrice = productService.calculateFinalPrice(originPrice, discountPrice);

        assertThat(finalPrice).isEqualTo(0);
    }



    static Promotion getCouponPromotion(LocalDateTime startDate, LocalDateTime endDate) {
        return Promotion.builder()
            .id(1)
            .promotion_type(COUPON)
            .discount_type(WON)
            .discount_value(30000)
            .use_started_at(startDate)
            .use_ended_at(endDate)
            .build();
    }

    static Promotion getCodePromotion(LocalDateTime startDate, LocalDateTime endDate) {
        return Promotion.builder()
            .id(2)
            .promotion_type(PromotionType.CODE)
            .discount_type(DiscountType.PERCENT)
            .discount_value(15)
            .use_started_at(startDate)
            .use_ended_at(endDate)
            .build();
    }
}