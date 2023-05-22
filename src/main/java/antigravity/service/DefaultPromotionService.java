package antigravity.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antigravity.domain.entity.Promotion;
import antigravity.repository.PromotionJpaRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DefaultPromotionService implements PromotionService {

	private final PromotionJpaRepository promotionJpaRepository;

	@Override
	public List<Promotion> findAllByIdsAndProductId(Long[] couponIds, Long productId) {
		return promotionJpaRepository.findAllByIdsAndProductId(couponIds, productId);
	}
}
