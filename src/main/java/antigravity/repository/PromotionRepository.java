package antigravity.repository;

import antigravity.domain.entity.Promotion;

public interface PromotionRepository {
	Promotion findById(int couponId);
}
