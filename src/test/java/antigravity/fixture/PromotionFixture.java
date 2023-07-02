package antigravity.fixture;

import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.common.DiscountType;
import antigravity.domain.entity.common.PromotionType;
import java.time.LocalDate;

public class PromotionFixture {
    public static Promotion getPromotion() {
        return Promotion.builder()
            .id(1)
            .promotionType(PromotionType.CODE)
            .name("15% 할인코드")
            .discountType(DiscountType.PERCENT)
            .discountValue(15)
            .useStartedAt(LocalDate.parse("2023-03-01"))
            .useEndedAt(LocalDate.parse("2024-03-01"))
            .build();
    }

    public static Promotion getPromotion2() {
        return Promotion.builder()
            .id(2)
            .promotionType(PromotionType.COUPON)
            .name("30000원 할인쿠폰")
            .discountType(DiscountType.WON)
            .discountValue(30000)
            .useStartedAt(LocalDate.parse("2023-03-01"))
            .useEndedAt(LocalDate.parse("2024-03-01"))
            .build();
    }

    public static Promotion getPromotion3() {
        return Promotion.builder()
            .id(3)
            .promotionType(PromotionType.CODE)
            .name("10% 할인코드")
            .discountType(DiscountType.PERCENT)
            .discountValue(10)
            .useStartedAt(LocalDate.parse("2023-03-01"))
            .useEndedAt(LocalDate.parse("2024-03-01"))
            .build();
    }

    public static Promotion getPromotion4() {
        return Promotion.builder()
            .id(4)
            .promotionType(PromotionType.COUPON)
            .name("50888원 할인쿠폰")
            .discountType(DiscountType.WON)
            .discountValue(50888)
            .useStartedAt(LocalDate.parse("2023-03-01"))
            .useEndedAt(LocalDate.parse("2024-03-01"))
            .build();
    }

    public static Promotion getExpiredPeriodPromotion() {
        return Promotion.builder()
            .id(5)
            .promotionType(PromotionType.COUPON)
            .name("name")
            .discountType(DiscountType.WON)
            .discountValue(10000)
            .useStartedAt(LocalDate.parse("2021-01-01"))
            .useEndedAt(LocalDate.parse("2021-04-01"))
            .build();
    }

    public static Promotion getMinPricePromotion() {
        return Promotion.builder()
            .id(6)
            .promotionType(PromotionType.COUPON)
            .name("name")
            .discountType(DiscountType.WON)
            .discountValue(206000)
            .useStartedAt(LocalDate.parse("2023-07-01"))
            .useEndedAt(LocalDate.parse("2023-07-25"))
            .build();
    }

    public static Promotion getExceedPricePromotion() {
        return Promotion.builder()
            .id(7)
            .promotionType(PromotionType.COUPON)
            .name("name")
            .discountType(DiscountType.WON)
            .discountValue(216000)
            .useStartedAt(LocalDate.parse("2023-03-01"))
            .useEndedAt(LocalDate.parse("2023-12-01"))
            .build();
    }
}
