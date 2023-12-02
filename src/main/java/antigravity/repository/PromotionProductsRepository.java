package antigravity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import antigravity.domain.entity.PromotionProducts;

@Repository
public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Integer> {
	List<PromotionProducts> findPromotionProductsByPromotionIdIn(List<Integer> promotionIds);
}
