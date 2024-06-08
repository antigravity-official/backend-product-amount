package antigravity.repository;

import antigravity.domain.entity.Product;
import antigravity.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Product getProduct(int id) {
        String query = "SELECT * FROM `product` WHERE id = :id ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        try {
            return namedParameterJdbcTemplate.queryForObject(
                    query,
                    params,
                    (rs, rowNum) -> Product.builder()
                            .id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .price(rs.getInt("price"))
                            .build()
            );
        } catch (EmptyResultDataAccessException e) {
            throw new ProductNotFoundException(id);
        }
    }
}
