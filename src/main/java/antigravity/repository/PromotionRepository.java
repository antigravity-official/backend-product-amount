package antigravity.repository;

import antigravity.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class PromotionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Promotion> getPromotion(final List<Integer> ids) {
        final String query = "SELECT * FROM `promotion` WHERE id IN (:ids)";
        final MapSqlParameterSource params = new MapSqlParameterSource("ids", ids);

        return jdbcTemplate.query(
                query,
                params,
                this::mapRowToPromotion
        );
    }

    public int updatePromotionUsedAt(final List<Integer> ids) {
        final String query = "UPDATE `promotion` SET used_at = :usedAt WHERE id IN (:ids)";
        final Map<String, ?> entries = Map.of(
                "usedAt", Date.valueOf(LocalDate.now()),
                "ids", ids
        );
        final MapSqlParameterSource params = new MapSqlParameterSource(entries);

        return jdbcTemplate.update(query, params);
    }

    private Promotion mapRowToPromotion(ResultSet rs, Integer rowNum) throws SQLException {
        Date usedAtDate = rs.getDate("used_at");
        LocalDate usedAt = (usedAtDate != null) ? usedAtDate.toLocalDate() : null;

        return Promotion.builder()
                .id(rs.getInt("id"))
                .promotion_type(rs.getString("promotion_type"))
                .name(rs.getString("name"))
                .discount_type(rs.getString("discount_type"))
                .discount_value(rs.getInt("discount_value"))
                .use_started_at(rs.getDate("use_started_at").toLocalDate())
                .use_ended_at(rs.getDate("use_ended_at").toLocalDate())
                .used_at(usedAt)
                .build();
    }
}