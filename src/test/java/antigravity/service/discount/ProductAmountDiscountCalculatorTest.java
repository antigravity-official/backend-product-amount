package antigravity.service.discount;

import antigravity.domain.Promotion;
import antigravity.error.BusinessException;
import antigravity.global.base.ServiceTestSupport;
import antigravity.service.discount.discountAmountCalculator.FixDiscountedAmountCalculator;
import antigravity.service.discount.discountAmountCalculator.ProductAmountDiscountCalculator;
import antigravity.service.discount.discountAmountCalculator.RateDiscountAmountCalculator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static antigravity.domain.DiscountType.*;
import static antigravity.error.ErrorCode.INVALID_PROMOTION_TYPE;
import static antigravity.error.ErrorCode.INVALID_DISCOUNT_PARAMETER;
import static antigravity.global.PromotionFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Disabled
@DisplayName("[Service] ProductAmountDiscountFactory - SpringBootTest")
public class ProductAmountDiscountCalculatorTest extends ServiceTestSupport {

    @Autowired
    ProductAmountDiscountFactory discountFactory;

    @Autowired
    FixDiscountedAmountCalculator fixCalculator;

    @Autowired
    RateDiscountAmountCalculator rateCalculator;

    @Nested
    @DisplayName("[ProductAmountDiscountFactory] discountType Enum 타입 매칭 테스트")
    class ProductAmountDiscountFactoryTest {

        @Test
        @DisplayName("[Success] discountType 'FIX' 정상 매칭")
        void successToMatchingWonType() throws Exception {
            //given
            Promotion fixPromotion = VALID_PROMOTION1.toEntity();
            Class<FixDiscountedAmountCalculator> expectedInstance = FixDiscountedAmountCalculator.class;

            //when
            ProductAmountDiscountCalculator fixCalc1 = discountFactory.calculateDiscountedAmount(WON);
            ProductAmountDiscountCalculator fixCalc2 = discountFactory.calculateDiscountedAmount(of(fixPromotion.getDiscountType()));
            //then
            assertAll(
                    () -> assertThat(fixCalc1).isInstanceOf(expectedInstance),
                    () -> assertThat(fixCalc2).isInstanceOf(expectedInstance)
            );

        }

        @Test
        @DisplayName("[Success] discountType 'RATE' 정상 매칭")
        void successToMatchingRateType() throws Exception {
            //given
            Promotion ratePromotion = VALID_PROMOTION2.toEntity();
            Class<RateDiscountAmountCalculator> expectedInstanse = RateDiscountAmountCalculator.class;

            //when
            ProductAmountDiscountCalculator rateCalc1 = discountFactory.calculateDiscountedAmount(PERCENT);
            ProductAmountDiscountCalculator rateCalc2 = discountFactory.calculateDiscountedAmount(of(ratePromotion.getDiscountType()));
            //then
            assertAll(
                    () -> assertThat(rateCalc1).isInstanceOf(expectedInstanse),
                    () -> assertThat(rateCalc2).isInstanceOf(expectedInstanse)
            );
        }

        @Test
        @DisplayName("[Exception] discountType 에서 WON / PERCENT 를 제외한 유효하지 않은 Type이 매핑될 경우 예외를 던진다.")
        void failByUnknownDiscountTypeMatched() throws Exception {
            //given
            Promotion invalidPromotion = UNKNOWN_TYPE_PROMOTION.toEntity();
            String expectedMessage = INVALID_PROMOTION_TYPE.getMessage();

            //when && then
            assertAll(
                    () -> assertThatThrownBy(
                            () -> discountFactory.calculateDiscountedAmount(of("이상한 타입")))
                            .isInstanceOf(BusinessException.class)
                            .hasMessageContaining(expectedMessage),
                    () -> assertThatThrownBy(
                            () -> discountFactory.calculateDiscountedAmount(of(invalidPromotion.getPromotionType())))
                            .isInstanceOf(BusinessException.class)
                            .hasMessageContaining(expectedMessage),
                    () -> assertThatThrownBy(
                            () -> discountFactory.calculateDiscountedAmount(INVALID))
                            .isInstanceOf(BusinessException.class)
                            .hasMessageContaining(expectedMessage)
            );
        }
    }

    @Nested
    @DisplayName("[FixDiscountCalculator] FixDiscountedAmountCalculator 정액 계산 테스트")
    class FixDiscountAmountCalculatorTest {

        @Test
        @DisplayName("[Success] discountType 'FIX' 정액 할인액 추출")
        void successToCalculateFixType() throws Exception {
            //given
            Promotion fixPromotion1 = VALID_PROMOTION1.toEntity();
            Promotion fixPromotion2 = VALID_PROMOTION4.toEntity();
            // when && then
            assertAll(
                    () -> assertThat(fixCalculator.applyDiscount(53369, fixPromotion1)).isEqualTo(fixPromotion1.getDiscountValue()),
                    () -> assertThat(fixCalculator.applyDiscount(63123, fixPromotion2)).isEqualTo(fixPromotion2.getDiscountValue())
            );
        }

        @Test
        @DisplayName("[Exception] 정액 할인 금액이 음수인 경우 예외를 던진다.")
        void failByMinusParameter() throws Exception {
            //given
            Promotion minusPromotion = MINUS_PROMOTION.toEntity();
            String ExpectedErrorMessage = INVALID_DISCOUNT_PARAMETER.getMessage();
            // when && then
            Assertions.assertThatThrownBy(() -> fixCalculator.applyDiscount(600000, minusPromotion))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining(ExpectedErrorMessage);
        }
    }

    @Nested
    @DisplayName("[RateDiscountCalculator] RateDiscountedAmountCalculator 정률 계산 테스트")
    class RateDiscountedAmountCalculatorTest {
        @Test
        @DisplayName("[Success] discountType 'RATE' 정률 할인 금액 추출")
        void successToCalculateRateType() throws Exception {
            //given
            Promotion ratePromotion1 = VALID_PROMOTION2.toEntity(); // 15% 할인쿠폰
            Promotion ratePromotion2 = VALID_PROMOTION3.toEntity(); // 35% 할인쿠폰

            // when && then
            assertAll(
                    () -> assertThat(rateCalculator.applyDiscount(53369, ratePromotion1)).isEqualTo(53369 / 100 * ratePromotion1.getDiscountValue()),
                    () -> assertThat(rateCalculator.applyDiscount(63123, ratePromotion2)).isEqualTo(63123 / 100 * ratePromotion2.getDiscountValue())
            );
        }

        @Test
        @DisplayName("[Exception] 정률 할인 비율이 음수, 0, 100% 이상일 경우 예외를 던진다.")
        void failByInvalidParameter() throws Exception {
            //given
            Promotion upperPromotion = SUPER_UPPER_PROMOTION.toEntity(); // 130% 할인 쿠폰 -> 사용하면 돈을 더 줘야하는 쿠폰..
            Promotion zeroPromotion = ZERO_PROMOTION.toEntity(); // 0% 할인 쿠폰 -> Can't Div to 0
            Promotion lowerPromotion = LOWER_PROMOTION.toEntity(); // -20% 할인 쿠폰 -> 오히려 비싸지는 쿠폰..
            String expectedMessage = INVALID_DISCOUNT_PARAMETER.getMessage();

            // when && then
            assertAll(
                    () -> assertThatThrownBy(
                            () -> rateCalculator.applyDiscount(53369, upperPromotion))
                            .isInstanceOf(BusinessException.class)
                            .hasMessageContaining(expectedMessage),
                    () -> assertThatThrownBy(
                            () -> rateCalculator.applyDiscount(53369, zeroPromotion))
                            .isInstanceOf(BusinessException.class)
                            .hasMessageContaining(expectedMessage),
                    () -> assertThatThrownBy(
                            () -> rateCalculator.applyDiscount(53369, lowerPromotion))
                            .isInstanceOf(BusinessException.class)
                            .hasMessageContaining(expectedMessage)
            );
        }
    }

    @Nested
    @DisplayName("[DiscountCalculator] 다양한 혼합 할인 케이스에 대한 계산 및 Exception 테스트")
    class MixedCalculatorTest {

        @Test
        @DisplayName("[applyDiscount] 복합 할인 케이스에 대한 결과를 정상적으로 리턴합니다. [정액+정액]")
        void successtoFixAndFix() throws Exception {
            //given

            //when


            //then


        }
    }
}
