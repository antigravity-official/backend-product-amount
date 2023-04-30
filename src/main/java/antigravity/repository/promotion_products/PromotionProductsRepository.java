package antigravity.repository.promotion_products;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import antigravity.domain.entity.product.Product;
import antigravity.domain.entity.promotion_products.PromotionProducts;

public interface PromotionProductsRepository extends JpaRepository<PromotionProducts, Integer> {

	@Query("select pp from PromotionProducts pp join fetch pp.promotion where pp.product = :product")
	List<PromotionProducts> findAllWithPromotionByProduct(@Param("product") Product product);

}
