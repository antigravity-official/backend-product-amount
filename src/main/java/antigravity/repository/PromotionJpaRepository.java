package antigravity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import antigravity.domain.entity.Promotion;

public interface PromotionJpaRepository extends JpaRepository<Promotion, Long> {

	@Query("select p from Promotion p join fetch PromotionProducts pp on p = pp.promotion where p.id = :promotionId and pp.product.id = :productId")
	Optional<Promotion> findByIdAndProductId(Long productId, Long promotionId);
}
