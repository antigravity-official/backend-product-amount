package antigravity.repository;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Repository
public class PromotionRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<Promotion> findPromotionByPromotionIdAndCouponId(int productId, int couponId) {
        String query = "select p.*\n" +
                        "from promotion p \n" +
                        "join promotion_products pp on pp.promotion_id = p.id \n" +
                        "where pp.product_id = :productId \n" +
                        "and p.id = :couponId ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        params.addValue("couponId", couponId);

        List<Promotion> results = namedParameterJdbcTemplate.query(
                query,
                params,
                (rs, rowNum) -> Promotion.builder()
                        .id(rs.getInt("id"))
                        .discount_type(Promotion.DiscountType.valueOf(rs.getString("discount_type")))
                        .promotion_type(Promotion.PromotionType.valueOf(rs.getString("promotion_type")))
                        .name(rs.getString("name"))
                        .discount_value(rs.getInt("discount_value"))
                        .use_started_at(rs.getDate("use_started_at"))
                        .use_ended_at(rs.getDate("use_ended_at"))
                        .build()
        );

        return ofNullable(results.isEmpty() ? null : results.get(0));
    }
}
