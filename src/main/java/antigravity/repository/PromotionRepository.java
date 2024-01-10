package antigravity.repository;

import antigravity.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class PromotionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<Promotion> getPromotion(final int[] ids) {
        final String query = "SELECT * FROM `promotion` WHERE id IN (:ids)";

        final MapSqlParameterSource params = new MapSqlParameterSource(
                "ids",
                arrayToList(ids)
        );

        return jdbcTemplate.query(
                query,
                params,
                this::mapRowToPromotion
        );
    }

    private Promotion mapRowToPromotion(final ResultSet rs, final Integer rowNum) throws SQLException {
        return Promotion.builder()
                .id(rs.getInt("id"))
                .promotion_type(rs.getString("promotion_type"))
                .name(rs.getString("name"))
                .discount_type(rs.getString("discount_type"))
                .discount_value(rs.getInt("discount_value"))
                .use_started_at(rs.getDate("use_started_at").toLocalDate())
                .use_ended_at(rs.getDate("use_ended_at").toLocalDate())
                .build();
    }

    private List<Integer> arrayToList(final int[] ids) {
        return Arrays.stream(ids)
                .boxed()
                .collect(Collectors.toList());
    }
}