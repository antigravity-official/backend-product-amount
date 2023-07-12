package antigravity.service.discount;


import antigravity.domain.DiscountType;
import antigravity.error.BusinessException;
import antigravity.error.ErrorCode;
import antigravity.global.base.ServiceTestSupport;
import antigravity.service.discount.discounted_amount.DiscountedAmountUtil;
import antigravity.service.discount.discounted_amount.FixDiscountedAmountService;
import antigravity.service.discount.discounted_amount.RateDiscountedAmountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static antigravity.domain.DiscountType.*;
import static antigravity.error.ErrorCode.INVALID_PROMOTION_TYPE;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("[Service] ProductAmountDiscountFactory - Service Layer Mock Test")
public class ProductAmountDiscountFactoryTest extends ServiceTestSupport {

    @InjectMocks
    ProductAmountDiscountFactory factory;

    @Nested
    @DisplayName("[calculateDiscountAmount] DiscountType에 맞는 할인 서비스를 리턴한다.")
    class TestProductAmountDiscountFactory {

        @Test
        @DisplayName("[Success] WON 타입 정상 매칭하고, FixDiscountedAmountService 인스턴스를 리턴한다.")
        void When_IsDiscountTypeWON_Expect_ReturnFixDiscountedAmountService() throws Exception {
            //given && when
            DiscountedAmountUtil fixDiscountedAmountService = factory.calculateDiscountedAmount(WON);
            //then
            Assertions.assertThat(fixDiscountedAmountService).isInstanceOf(FixDiscountedAmountService.class);
        }

        @Test
        @DisplayName("[Success] PERCENT 타입 정상 매칭하고, RateDiscountedAmountService 인스턴스를 리턴한다.")
        void When_IsDiscountTypePERCENT_Expect_ReturnFixDiscountedAmountService() throws Exception {
            //given && when
            DiscountedAmountUtil rateDiscountedAmountService = factory.calculateDiscountedAmount(PERCENT);
            //then
            Assertions.assertThat(rateDiscountedAmountService).isInstanceOf(RateDiscountedAmountService.class);
        }

        @Test
        @DisplayName("[Exception] WON과 PERCENT 타입이 아니라면 예외를 던진다.")
        void When_IsDiscountTypeINVALID_Expect_ThrowException() throws Exception {
            //given && when && then
            assertAll(
                    () -> assertThatThrownBy(() -> factory.calculateDiscountedAmount(INVALID))
                            .isInstanceOf(BusinessException.class)
                            .hasMessageContaining(INVALID_PROMOTION_TYPE.getMessage()),
                    () -> assertThatThrownBy(() -> factory.calculateDiscountedAmount(of("Unknown Type")))
                            .isInstanceOf(BusinessException.class)
                            .hasMessageContaining(INVALID_PROMOTION_TYPE.getMessage())
            );
        }

    }
}
