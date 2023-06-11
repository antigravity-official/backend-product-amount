package antigravity.repository;

import antigravity.domain.entity.Product;
import antigravity.enums.ErrorResponse;
import antigravity.exception.CommonException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Product getProduct(int id) throws Exception {
        String query = "SELECT * FROM `product` WHERE id = :id ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        Product product = null;
        try {
            product = namedParameterJdbcTemplate.queryForObject(
                    query,
                    params,
                    (rs, rowNum) -> Product.builder()
                            .id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .price(rs.getInt("price"))
                            .build()
            );
        } catch (EmptyResultDataAccessException e) {
            throw new CommonException(ErrorResponse.PRODUCT_ID_NOT_FOUND);
        } catch (Exception e) {
            throw new CommonException(ErrorResponse.PRODUCT_NOT_FOUND);
        }

        return product;

    }
}
