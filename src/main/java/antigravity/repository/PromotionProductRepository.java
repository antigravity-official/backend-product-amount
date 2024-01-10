package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class PromotionProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<PromotionProducts> getPromotionProduct(final int id) {
        final String query = "SELECT * FROM `promotion_products` WHERE product_id = :id";
        final MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbcTemplate.query(query, params, this::mapRowToPromotionProduct);
    }

    private PromotionProducts mapRowToPromotionProduct(final ResultSet rs, final Integer rowNum) throws SQLException {
        return PromotionProducts.builder()
                .id(rs.getInt("id"))
                .promotionId(rs.getInt("promotion_id"))
                .productId(rs.getInt("product_id"))
                .build();
    }
}
