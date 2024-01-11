package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import antigravity.utils.RepositoryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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
    public List<PromotionProducts> getPromotionProduct(int id) {
        final String query = "SELECT * FROM `promotion_products` WHERE product_id = :id";
        final MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbcTemplate.query(
                query,
                params,
                RepositoryUtils::mapRowToPromotionProduct);
    }
}