package antigravity.repository;

import antigravity.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PromotionJdbcRepository implements PromotionRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Promotion> findValidPromotionsByProductId(int productId) {
        String nowDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String query = "SELECT pm.id, pm.promotion_type, pm.name, pm.discount_type, pm.discount_value, pm.use_started_at, pm.use_ended_at " +
                "FROM promotion pm " +
                "INNER JOIN promotion_products pp ON pp.promotion_id = pm.id AND pp.product_id = :productId " +
                "WHERE pm.use_started_at <= :nowDate AND pm.use_ended_at >= :nowDate " +
                "ORDER BY pm.id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        params.addValue("nowDate", nowDate);

        return namedParameterJdbcTemplate.query(
                query,
                params,
                (rs, rowNum) ->
                        Promotion.builder()
                                .id(rs.getInt("id"))
                                .promotion_type(rs.getString("promotion_type"))
                                .name(rs.getString("name"))
                                .discount_type(rs.getString("discount_type"))
                                .discount_value(rs.getInt("discount_value"))
                                .use_started_at(rs.getDate("use_started_at"))
                                .use_ended_at(rs.getDate("use_ended_at"))
                            .build()
        );
    }
}
