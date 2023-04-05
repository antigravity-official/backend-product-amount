package antigravity.domain.promotion;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static antigravity.domain.promotion.PromotionFixture.aPromotion;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PromotionTest {

    @Test
    @DisplayName("100,000원 정액 쿠폰을 300,000원에 적용할 경우 200,000원을 반환한다.")
    void applyTest_1() {
        // given
        Promotion promotion = aPromotion()
                .promotionType(PromotionType.COUPON)
                .discountType(DiscountType.WON)
                .discountValue(100_000)
            .build();

        // when
        long result = promotion.apply(300_000);

        // then
        assertEquals(200_000, result);
    }

    @Test
    @DisplayName("15% 정률 프로모션을 적용할 경우 85,000원을 반환한다.")
    void applyTest_2() {
        // given
        Promotion promotion = aPromotion()
                .promotionType(PromotionType.CODE)
                .discountType(DiscountType.PERCENT)
                .discountValue(15)
                .build();

        // when
        long result = promotion.apply(100_000);

        // then
        assertEquals(85_000, result);
    }
}
