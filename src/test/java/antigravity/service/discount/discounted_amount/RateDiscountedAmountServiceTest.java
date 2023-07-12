package antigravity.service.discount.discounted_amount;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import antigravity.service.discount.discounted_amount.RateDiscountedAmountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static antigravity.error.ErrorCode.INVALID_DISCOUNT_PARAMETER;
import static antigravity.global.fixture.PromotionFixture.*;

@DisplayName("[Service] RateDiscountedAmount - Service Layer Mock Test")
public class RateDiscountedAmountServiceTest extends ServiceTestSupport {

    @InjectMocks
    RateDiscountedAmountService discountService;

    @Nested
    @DisplayName("[getDiscountedValue] 정률 할인액[RATE] 산정 비즈니스 로직")
    class TestGetDiscountedValue {

        @Test
        @DisplayName("[Success] 정상적인 할인 인자(0 < positiveInt <= 100)가 입력되어, 할인액을 리턴한다.")
        void When_ValidDiscountedValueRequested_Expect_Return_DiscountedValue() throws Exception {
            //given
            Promotion promotion = VALID_RATE_PROMOTION2.toEntity();
            //when
            int discountedValue = discountService.getDiscountedValue(100000, promotion);
            //then
            Assertions.assertThat(discountedValue).isEqualTo(15000);
        }

        @Test
        @DisplayName("[Success] 비정상적인 할인 인자(0)가 입력되어, 예외를 던진다.")
        void When_ZeroDiscountedValueRequested_Expect_ThrowException() throws Exception {
            //given
            Promotion promotion = ZERO_PROMOTION.toEntity();
            //when && then
            Assertions.assertThatThrownBy(() -> discountService.getDiscountedValue(100000, promotion))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(INVALID_DISCOUNT_PARAMETER.getMessage());
        }

        @Test
        @DisplayName("[Success] 비정상적인 할인 인자(negative Int)가 입력되어, 예외를 던진다.")
        void When_NegativeDiscountedValueRequested_Expect_ThrowException() throws Exception {
            //given
            Promotion promotion = MINUS_RATE_PROMOTION.toEntity();
            //when && then
            Assertions.assertThatThrownBy(() -> discountService.getDiscountedValue(100000, promotion))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(INVALID_DISCOUNT_PARAMETER.getMessage());
        }


    }
}
