package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PromotionProductsRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<PromotionProducts> getPromotionsProducts(int productId) {
        String query = "SELECT * FROM `promotion_products` WHERE product_id = :productId ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);

        return namedParameterJdbcTemplate.queryForList(
                query,
                params,
                PromotionProducts.class
        );
    }
}
