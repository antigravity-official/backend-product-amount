package antigravity.repository;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import antigravity.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PromotionRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<Promotion> getPromotionById(int productId, int promotionId) {
        String query = "SELECT a.* FROM `promotion` AS a JOIN `promotion_products` AS b ON a.id = b.promotion_id AND b.product_id = :productId WHERE a.id = :promotionId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        params.addValue("promotionId", promotionId);
        Promotion promotion;
        try {
            promotion = namedParameterJdbcTemplate.queryForObject(
                query,
                params,
                (rs, rowNum) -> Promotion.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .promotion_type(rs.getString("promotion_type"))
                    .discount_type(rs.getString("discount_type"))
                    .discount_value(rs.getInt("discount_value"))
                    .use_started_at(rs.getTimestamp("use_started_at").toLocalDateTime())
                    .use_ended_at(rs.getTimestamp("use_ended_at").toLocalDateTime())
                    .build()
            );
        } catch (EmptyResultDataAccessException e) {
            promotion = null;
        }

        return Optional.ofNullable(promotion);
    }
}
