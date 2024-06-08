package antigravity.repository;

import antigravity.domain.entity.Promotion;

import java.util.List;

public interface PromotionRepository {
    List<Promotion> findValidPromotionsByProductId(int productId);
}
