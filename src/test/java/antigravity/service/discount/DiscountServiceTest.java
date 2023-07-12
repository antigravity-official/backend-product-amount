package antigravity.service.discount;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import antigravity.global.dto.response.ProductAmountResponse;
import antigravity.service.discount.discounted_amount.DiscountedAmountUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static antigravity.domain.DiscountType.PERCENT;
import static antigravity.domain.DiscountType.WON;
import static antigravity.error.ErrorCode.BELOW_LOWER_LIMIT;
import static antigravity.error.ErrorCode.EXCEEDS_UPPER_LIMIT;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * DiscountService
 * Service Layer - MockTest
 */
@DisplayName("[Service] DiscountService - Service Layer Mock Test")
public class DiscountServiceTest extends ServiceTestSupport {

    @Mock
    ProductAmountDiscountFactory factory;

    @InjectMocks
    private DiscountService discountService;

    Product mockProduct;
    Promotion mockFixPromotion;
    Promotion mockRatePromotion;
    List<Promotion> mockPromotions;
    DiscountedAmountUtil mockFixUtil;
    DiscountedAmountUtil mockRateUtil;

    @BeforeEach
    void initSetting() {
        mockProduct = mock(Product.class);
        mockFixPromotion = mock(Promotion.class);
        mockRatePromotion = mock(Promotion.class);
        mockFixUtil = mock(DiscountedAmountUtil.class);
        mockRateUtil = mock(DiscountedAmountUtil.class);
        mockPromotions = of(mockFixPromotion, mockRatePromotion);

        given(mockFixPromotion.getDiscountType()).willReturn("WON");
        given(mockRatePromotion.getDiscountType()).willReturn("PERCENT");

        given(factory.calculateDiscountedAmount(WON))
                .willReturn(mockFixUtil);
        given(factory.calculateDiscountedAmount(PERCENT))
                .willReturn(mockRateUtil);
    }

    @Nested
    @DisplayName("[applyDiscount] 프로모션 리스트를 적용한, 가격 응답을 리턴합니다.")
    class FinalDiscountedPriceTest {

        @Test
        @DisplayName("[Success] 정상적인 할인 요청에 응답을 내립니다. [ 정액 + 정률 ]")
        void When_ValidProductAndPromotionRequested_Expect_ReturnResponse() throws Exception {
            //given - @BeforeEach
            given(mockProduct.getName()).willReturn("Mock Data");
            given(mockProduct.getPrice()).willReturn(100000);
            given(mockFixUtil.getDiscountedValue(mockProduct.getPrice(), mockFixPromotion))
                    .willReturn(10000);
            given(mockRateUtil.getDiscountedValue(mockProduct.getPrice(), mockRatePromotion))
                    .willReturn(15000);

            //when
            ProductAmountResponse appliedResponse = discountService.applyDiscount(mockProduct, mockPromotions);

            //then
            assertAll(
                    () -> assertThat(appliedResponse.getName()).isEqualTo("Mock Data"),
                    () -> assertThat(appliedResponse.getOriginPrice()).isEqualTo(100000),
                    () -> assertThat(appliedResponse.getDiscountAmount()).isEqualTo(25000),
                    () -> assertThat(appliedResponse.getFinalPrice()).isEqualTo(75000)
            );
        }

        @Test
        @DisplayName("[Success] 할인 적용 후 최종 가격이 백 원 단위로 남으면, 천 원 단위로 절삭한다.")
        void When_FinalPriceInRemainderInTheHundredsDigit_Expect_CutOffToThousand() throws Exception {
            //given
            given(mockProduct.getName()).willReturn("Mock Data");
            given(mockProduct.getPrice()).willReturn(100000);
            given(mockFixUtil.getDiscountedValue(mockProduct.getPrice(), mockFixPromotion))
                    .willReturn(13500);
            given(mockRateUtil.getDiscountedValue(mockProduct.getPrice(), mockRatePromotion))
                    .willReturn(12300);

            //when
            ProductAmountResponse appliedResponse = discountService.applyDiscount(mockProduct, mockPromotions);

            //then
            assertAll(
                    () -> assertThat(appliedResponse.getName()).isEqualTo("Mock Data"),
                    () -> assertThat(appliedResponse.getOriginPrice()).isEqualTo(100000),
                    () -> assertThat(appliedResponse.getDiscountAmount()).isEqualTo(26000),
                    () -> assertThat(appliedResponse.getFinalPrice()).isEqualTo(74000)
            );
        }


        @Test
        @DisplayName("[Exception] 할인 적용 후 최종 가격이 LOWER_BOUND (10,000 KRW) 미만이면 예외를 던진다.")
        void When_HasLowerValueThanLowerBound_Expect_ThrowException() throws Exception {
            //given
            given(mockProduct.getPrice()).willReturn(100000);
            given(mockFixUtil.getDiscountedValue(mockProduct.getPrice(), mockFixPromotion))
                    .willReturn(80000);
            given(mockRateUtil.getDiscountedValue(mockProduct.getPrice(), mockRatePromotion))
                    .willReturn(15000);
            //when && then
            assertThatThrownBy(() -> discountService.applyDiscount(mockProduct, mockPromotions))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(BELOW_LOWER_LIMIT.getMessage());
        }

        @Test
        @DisplayName("[Exception] 할인 적용 후 최종 가격이 UPPER_BOUND (10,000,000 KRW) 초과면 예외를 던진다.")
        void When_HasHigherValueThanUpperBound_Expect_ThrowException() throws Exception {
            //given
            given(mockProduct.getPrice()).willReturn(20000000);
            given(mockFixUtil.getDiscountedValue(mockProduct.getPrice(), mockFixPromotion))
                    .willReturn(80000);
            given(mockRateUtil.getDiscountedValue(mockProduct.getPrice(), mockRatePromotion))
                    .willReturn(15000);
            //when && then
            assertThatThrownBy(() -> discountService.applyDiscount(mockProduct, mockPromotions))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(EXCEEDS_UPPER_LIMIT.getMessage());
        }
    }
}
