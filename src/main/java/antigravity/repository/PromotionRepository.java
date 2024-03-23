package antigravity.repository;

import antigravity.domain.entity.promotion.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

}
