package antigravity.repository.promotionproducts;

import antigravity.domain.PromotionProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface PromotionProductsQueryRepository extends JpaRepository<PromotionProducts, Integer> {
    @Query("SELECT p.promotionId FROM PromotionProducts p WHERE p.productId = :productId")
    int[] findPromotionIdsByProductId(@Param("productId") Integer productId);
}
