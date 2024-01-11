package antigravity.repository;

import antigravity.domain.entity.Product;
import antigravity.utils.RepositoryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for performing database operations on Product entities.
 */
@RequiredArgsConstructor
@Repository
public class ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return An Optional containing the product if found, or an empty Optional otherwise.
     */
    public Optional<Product> getProduct(int id) {
        final String query = "SELECT * FROM `product` WHERE id = :id";
        final MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                query,
                params,
                RepositoryUtils::mapRowToProduct
        ));
    }
}