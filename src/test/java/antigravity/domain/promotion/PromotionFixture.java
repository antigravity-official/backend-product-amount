package antigravity.domain.promotion;

import java.time.LocalDateTime;

import static java.util.Arrays.asList;

public class PromotionFixture {
    public static Promotion.PromotionBuilder aPromotion() {
        return Promotion.builder()
                .promotionType(PromotionType.COUPON)
                .name("name")
                .discountType(DiscountType.WON)
                .discountValue(10_000)
                .useStartedAt(LocalDateTime.now())
                .useEndedAt(LocalDateTime.now())
                .productIds(asList(1L, 2L));
    }
}
