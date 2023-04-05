package antigravity.domain.promotion;

import java.time.LocalDateTime;
import java.util.List;

public interface PromotionRepository {
    List<Promotion> findByProductIdAndPromotionIdsAndUseAtBetween(long productId, List<Long> promotionIds, LocalDateTime useAt);
}
