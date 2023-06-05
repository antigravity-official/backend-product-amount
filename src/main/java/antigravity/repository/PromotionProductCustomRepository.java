package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;

import java.util.List;

public interface PromotionProductCustomRepository {
    List<PromotionProducts> fetch(Integer[] promotionId, Integer productId);
}
