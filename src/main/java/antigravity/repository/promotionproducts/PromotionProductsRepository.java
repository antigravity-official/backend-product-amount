package antigravity.repository.promotionproducts;

import antigravity.domain.PromotionProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Integer> {
}
