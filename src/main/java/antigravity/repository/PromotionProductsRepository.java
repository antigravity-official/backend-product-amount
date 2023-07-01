package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Integer> {

    @EntityGraph(attributePaths = {"promotion"})
    List<PromotionProducts> findWithPromotionByPromotionIdIn(List<Integer> couponIds);

}
