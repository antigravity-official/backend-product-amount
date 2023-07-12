package antigravity.service.discount;

import antigravity.domain.Product;
import antigravity.domain.Promotion;
import antigravity.global.dto.response.ProductAmountResponse;
import antigravity.global.base.ServiceTestSupport;
import antigravity.service.discount.discounted_amount.DiscountedAmountUtil;
import antigravity.service.discount.discounted_amount.FixDiscountedAmountService;
import antigravity.service.discount.discounted_amount.RateDiscountedAmountService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;

import static antigravity.domain.DiscountType.*;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


@DisplayName("[Service] DiscountService - SpringBootTest")
public class DiscountServiceTest extends ServiceTestSupport {

    @Mock
    ProductAmountDiscountFactory factory;

    @Mock
    FixDiscountedAmountService fixService;

    @Mock
    RateDiscountedAmountService rateService;

    @Spy
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

        given(mockProduct.getName()).willReturn("Mock Data");
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

    }
}
