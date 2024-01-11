package antigravity.repository;

import antigravity.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    public Optional<Product> getProduct(final int id) {
        final String query = "SELECT * FROM `product` WHERE id = :id";
        final MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                query,
                params,
                this::mapRowToProduct
        ));
    }

    /**
     * Maps a row in the result set to a Product object.
     *
     * @param rs The ResultSet to map.
     * @param rowNum The number of the current row.
     * @return A Product object corresponding to the current row.
     * @throws SQLException If an SQL error occurs.
     */
    private Product mapRowToProduct(final ResultSet rs, final Integer rowNum) throws SQLException {
        return Product.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .price(rs.getInt("price"))
                .build();
    }
}