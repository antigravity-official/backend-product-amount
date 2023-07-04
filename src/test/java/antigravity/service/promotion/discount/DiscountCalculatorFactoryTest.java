package antigravity.service.promotion.discount;

import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import antigravity.service.discount.DiscountCalculator;
import antigravity.service.discount.DiscountCalculatorFactory;
import antigravity.service.discount.FixDiscountCalculator;
import antigravity.service.discount.RateDiscountCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static antigravity.error.ErrorCode.INVALID_PROMOTION_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("[SpringBootTest] DiscountPolicyFactory Test")
public class DiscountCalculatorFactoryTest extends ServiceTestSupport {

    /**
     * 예외 테스트 1
     * "WON", "PERCENT"를 제외한 이상값이 있는 비정상 프로모션 타입 매핑
     */
    @Test
    @DisplayName("[DiscountPolicyFactory] - WON, PERCENT 타입을 제외한 다른 타입이 들어오면 예외를 던진다.")
    void discountPolicyWithInvalidType() {
        // given
        DiscountCalculatorFactory discountCalculatorFactory = new DiscountCalculatorFactory();
        String invalidDiscountType = "이상한_타입";

        // when & then
        assertThatThrownBy(() -> discountCalculatorFactory.createDiscountPolicy(invalidDiscountType))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(INVALID_PROMOTION_TYPE.getMessage());
    }

    @Test
    @DisplayName("[DiscountPolicyFactory] - WON 할인 타입으로 DiscountPolicy -> FixDiscountPolicy로 구체화")
    void testCreateDiscountPolicyWithWonType() {
        // given
        DiscountCalculatorFactory discountCalculatorFactory = new DiscountCalculatorFactory();
        String discountType = "WON";

        // when
        DiscountCalculator discountPolicy = discountCalculatorFactory.createDiscountPolicy(discountType);

        // then
        assertThat(discountPolicy).isInstanceOf(FixDiscountCalculator.class);
    }

    @Test
    @DisplayName("[DiscountPolicyFactory] - PERCENT 할인 타입으로 DiscountPolicy -> RateDiscountPolicy로 구체화")
    void testCreateDiscountPolicyWithPercentType() {
        // given
        DiscountCalculatorFactory discountCalculatorFactory = new DiscountCalculatorFactory();
        String discountType = "PERCENT";

        // when
        DiscountCalculator discountPolicy = discountCalculatorFactory.createDiscountPolicy(discountType);

        // then
        assertThat(discountPolicy).isInstanceOf(RateDiscountCalculator.class);
    }

}
