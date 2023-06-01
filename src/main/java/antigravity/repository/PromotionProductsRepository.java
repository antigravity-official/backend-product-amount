package antigravity.repository;

import antigravity.domain.PromotionProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Integer> {
    @Query("SELECT p.promotionId FROM PromotionProducts p WHERE p.productId = :productId")
    int[] findPromotionIdsByProductId(@Param("productId") Integer productId);
}
