package antigravity.service;

import java.util.List;

import antigravity.domain.entity.Promotion;

public interface PromotionService {
	List<Promotion> findAllByIdsAndProductId(Long[] couponIds, Long productId);
}
