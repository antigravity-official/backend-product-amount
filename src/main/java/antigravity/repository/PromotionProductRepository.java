package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository for performing database operations on PromotionProducts entities.
 */
@RequiredArgsConstructor
@Repository
public class PromotionProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Retrieves a list of PromotionProducts associated with a given product ID.
     *
     * @param id The ID of the product for which to retrieve the promotion products.
     * @return A List of PromotionProducts associated with the specified product ID.
     */
    public List<PromotionProducts> getPromotionProduct(final int id) {
        final String query = "SELECT * FROM `promotion_products` WHERE product_id = :id";
        final MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbcTemplate.query(query, params, this::mapRowToPromotionProduct);
    }

    /**
     * Maps a row in the result set to a PromotionProducts object.
     *
     * @param rs The ResultSet to map.
     * @param rowNum The number of the current row.
     * @return A PromotionProducts object corresponding to the current row.
     * @throws SQLException If an SQL error occurs.
     */
    private PromotionProducts mapRowToPromotionProduct(final ResultSet rs, final Integer rowNum) throws SQLException {
        return PromotionProducts.builder()
                .id(rs.getInt("id"))
                .promotionId(rs.getInt("promotion_id"))
                .productId(rs.getInt("product_id"))
                .build();
    }
}