package antigravity.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;
import antigravity.exception.NotFoundResourceException;
import antigravity.repository.PromotionJpaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DefaultPromotionService implements PromotionService {

	private final PromotionJpaRepository promotionJpaRepository;

	@Override
	public Promotion getByIdAndProductId(Long productId,Long couponId) {
		return promotionJpaRepository.findByIdAndProductId(productId, couponId)
			.orElseThrow(() -> new NotFoundResourceException(PromotionProducts.class));
	}
}
