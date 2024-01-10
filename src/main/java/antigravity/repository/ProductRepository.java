package antigravity.repository;

import antigravity.domain.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Optional<Product> getProduct(final int id) {
        final String query = "SELECT * FROM `product` WHERE id = :id";
        final MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return Optional.ofNullable(jdbcTemplate.queryForObject(
                query,
                params,
                this::mapRowToProduct
        ));
    }

    private Product mapRowToProduct(final ResultSet rs, final Integer rowNum) throws SQLException {
        return Product.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .price(rs.getInt("price"))
                .build();
    }
}