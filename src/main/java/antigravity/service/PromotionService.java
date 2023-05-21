package antigravity.service;

import antigravity.domain.entity.Promotion;

public interface PromotionService {

	Promotion getByIdAndProductId(Long productId,Long couponId);

}
