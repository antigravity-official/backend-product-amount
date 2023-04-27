package antigravity.rds.repository;

import antigravity.rds.entity.PromotionProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionProductRepository extends JpaRepository<PromotionProducts, Integer> {
    Optional<PromotionProducts> findByPromotionIdAndProductId(Integer promotionId, Integer productId);
}
