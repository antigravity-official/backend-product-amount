package antigravity.repository;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import antigravity.domain.entity.PromotionProducts;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PromotionProductsRepository {
	
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public List<PromotionProducts> getPromotionProducts(int id) {
		
		String query = "SELECT * FROM promotion_products WHERE product_id = :id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		
		List<PromotionProducts> promotionProductsList = 
				namedParameterJdbcTemplate.query(
						query, 
						params,
						(rs, rowNum) -> PromotionProducts.builder()
									.id(rs.getInt("id"))
									.promotionId(rs.getInt("promotion_id"))
									.productId(rs.getInt("product_id"))
									.build());
		
		return promotionProductsList;
		
	} 

}
