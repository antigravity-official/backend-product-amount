package antigravity.domain.promotion;

import antigravity.service.product.PromotionApplyComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static antigravity.domain.promotion.DiscountType.PERCENT;
import static antigravity.domain.promotion.DiscountType.WON;
import static antigravity.domain.promotion.PromotionFixture.aPromotion;
import static antigravity.domain.promotion.PromotionType.COUPON;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PromotionApplyComparatorTest {

    @Test
    @DisplayName("정액 프로모션이 정률 프로모션보다 우선순위가 높음")
    void sortTest_1() {
        // given
        List<Promotion> promotions = asList(
                aPromotion()
                        .promotionType(COUPON)
                        .discountType(WON)
                        .discountValue(100_000)
                        .build(),
                aPromotion()
                        .promotionType(COUPON)
                        .discountType(PERCENT)
                        .discountValue(15)
                        .build()
        );

        // when
        PromotionApplyComparator promotionApplyComparator = new PromotionApplyComparator();
        Collections.sort(promotions, promotionApplyComparator);

        // then
        assertEquals(WON, promotions.get(0).getDiscountType());
        assertEquals(PERCENT, promotions.get(1).getDiscountType());
    }

    @Test
    @DisplayName("정액 프로모션이 2개 존재할 경우 할인율이 높은 순서대로 정렬")
    void sortTest_2() {
        // given
        List<Promotion> promotions = asList(
                aPromotion()
                        .promotionType(COUPON)
                        .discountType(WON)
                        .discountValue(100_000)
                        .build(),
                aPromotion()
                        .promotionType(COUPON)
                        .discountType(WON)
                        .discountValue(200_000)
                        .build()
        );

        // when
        PromotionApplyComparator promotionApplyComparator = new PromotionApplyComparator();
        Collections.sort(promotions, promotionApplyComparator);

        // then
        assertEquals(200_000, promotions.get(0).getDiscountValue());
        assertEquals(100_000, promotions.get(1).getDiscountValue());
    }

    @Test
    @DisplayName("정률 프로모션이 2개 존재할 경우 할인율이 높은 순서대로 정렬")
    void sortTest_3() {
        // given
        List<Promotion> promotions = asList(
                aPromotion()
                        .promotionType(COUPON)
                        .discountType(PERCENT)
                        .discountValue(1)
                        .build(),
                aPromotion()
                        .promotionType(COUPON)
                        .discountType(PERCENT)
                        .discountValue(15)
                        .build()
        );

        // when
        PromotionApplyComparator promotionApplyComparator = new PromotionApplyComparator();
        Collections.sort(promotions, promotionApplyComparator);

        // then
        assertEquals(15, promotions.get(0).getDiscountValue());
        assertEquals(1, promotions.get(1).getDiscountValue());
    }
}
