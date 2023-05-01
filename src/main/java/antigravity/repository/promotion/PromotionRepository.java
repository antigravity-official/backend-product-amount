package antigravity.repository.promotion;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import antigravity.domain.entity.product.Product;
import antigravity.domain.entity.promotion.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

	@Query("select p from Promotion p join PromotionProducts pp on p.id = pp.promotion.id "
		+ "where pp.product = :product and p.id in :couponIds and p.useStartedAt <= :now and p.useEndedAt >= :now")
	List<Promotion> findAllByProductAndCouponIds(
		@Param("product") Product product, @Param("couponIds") Integer[] couponIds, @Param("now") Timestamp now);

}
