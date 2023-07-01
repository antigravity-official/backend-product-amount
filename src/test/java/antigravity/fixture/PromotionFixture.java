package antigravity.fixture;

import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.common.DiscountType;
import antigravity.domain.entity.common.PromotionType;
import java.time.LocalDate;

public class PromotionFixture {
    public static Promotion getPromotion() {
        return Promotion.builder()
            .promotionType(PromotionType.COUPON)
            .name("name")
            .discountType(DiscountType.WON)
            .discountValue(10000)
            .useStartedAt(LocalDate.parse("2023-03-01"))
            .useEndedAt(LocalDate.parse("2024-03-01"))
            .build();
    }

    public static Promotion getExpiredPromotionPeriod() {
        return Promotion.builder()
            .promotionType(PromotionType.COUPON)
            .name("name")
            .discountType(DiscountType.WON)
            .discountValue(10000)
            .useStartedAt(LocalDate.parse("2021-01-01"))
            .useEndedAt(LocalDate.parse("2021-01-01"))
            .build();
    }
}
