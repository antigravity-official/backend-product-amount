package antigravity.service.discount.discountedamount;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static antigravity.error.ErrorCode.INVALID_DISCOUNT_PARAMETER;
import static antigravity.global.fixture.PromotionFixture.*;

@DisplayName("[Service] FixDiscountedAmount - Service Layer Mock Test")
class FixDiscountedAmountServiceTest extends ServiceTestSupport {

    @InjectMocks
    FixDiscountedAmountService discountService;

    @Nested
    @DisplayName("[getDiscountedValue] 정액 할인액[FIX] 산정 비즈니스 로직")
    class TestGetDiscountedValue {

        @Test
        @DisplayName("[Success] 정상적인 할인 인자(positiveInt)가 입력되어, 할인액을 리턴한다.")
        void When_ValidDiscountedValueRequested_Expect_Return_DiscountedValue() throws Exception {
            //given
            Promotion promotion = VALID_FIX_PROMOTION1.toEntity();
            //when
            int discountedValue = discountService.getDiscountedValue(100000, promotion);
            //then
            Assertions.assertThat(discountedValue).isEqualTo(30000);
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
            Promotion promotion = MINUS_FIX_PROMOTION.toEntity();
            //when && then
            Assertions.assertThatThrownBy(() -> discountService.getDiscountedValue(100000, promotion))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(INVALID_DISCOUNT_PARAMETER.getMessage());
        }


    }
}
