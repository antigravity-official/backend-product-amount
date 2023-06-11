package antigravity.repository;

import antigravity.domain.entity.Promotion;
import antigravity.enums.ErrorResponse;
import antigravity.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PromotionRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Promotion getPromotion(int id) throws Exception {

        String query = "SELECT * FROM PROMOTION WHERE id = :id ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        Promotion promotion = null;

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
            throw new CommonException(ErrorResponse.PROMOTION_ID_NOT_FOUND);
        } catch (Exception e) {
            throw new CommonException(ErrorResponse.PROMOTION_NOT_FOUND);
        }

        return promotion;
    }
}
