package antigravity.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import antigravity.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PromotionRepository {
	
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public Promotion getPromotions(int id) {
		
		String query = "SELECT * FROM promotion WHERE id = :id";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		params.addValue("id", id);
		
		Promotion promotions = namedParameterJdbcTemplate.queryForObject(
				query, 
				params, 
				(rs, rowNum) -> Promotion.builder()
							.id(rs.getInt("id"))
							.promotion_type(rs.getString("promotion_type"))
							.name(rs.getString("name"))
							.discount_value(rs.getInt("discount_value"))
							.use_started_at(rs.getDate("use_started_at"))
							.use_ended_at(rs.getDate("use_ended_at"))
							.build()
				);
		
		return promotions;
	}
}
