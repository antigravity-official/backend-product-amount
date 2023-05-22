package antigravity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import antigravity.domain.entity.Promotion;

public interface PromotionJpaRepository extends JpaRepository<Promotion, Long> {

	@Query("select p from Promotion p join fetch PromotionProducts pp on p = pp.promotion where pp.product.id = :productId and p.id in :couponIds")
	List<Promotion> findAllByIdsAndProductId(Long[] couponIds, Long productId);
}
