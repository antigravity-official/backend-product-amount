package antigravity.domain.reader;

import antigravity.domain.entity.Promotion;

public interface PromotionReader {

    Promotion getPromotionByPromotionIdAndCouponId(int productId, int couponId);
}
