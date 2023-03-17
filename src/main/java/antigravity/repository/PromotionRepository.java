package antigravity.repository;

import antigravity.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PromotionRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<Promotion> getPromotion(int couponId){
        String query = "SELECT * FROM `promotion` WHERE id = :couponId ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("couponId", couponId);

        Promotion promotion;
        try {
            promotion = namedParameterJdbcTemplate.queryForObject(
                    query,
                    params,
                    (rs, rowNum) -> Promotion.builder()
                            .id(rs.getInt("id"))
                            .promotion_type(rs.getString("promotion_type"))
                            .name(rs.getString("name"))
                            .discount_type(rs.getString("discount_type"))
                            .discount_value(rs.getInt("discount_value"))
                            .use_started_at(rs.getDate("use_started_at"))
                            .use_ended_at(rs.getDate("use_ended_at"))
                            .build()
            );
        } catch (EmptyResultDataAccessException e) {
            promotion = null;
        }

        return Optional.ofNullable(promotion);
    }
}
