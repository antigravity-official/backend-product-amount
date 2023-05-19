package antigravity.repository;

import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

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

        return namedParameterJdbcTemplate.queryForObject(
                query,
                params,
                (rs, rowNum) -> Product.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .price(rs.getInt("price"))
                        .build()
        );
    }


    public Promotion findCouponName(int id) {
        String query = "SELECT * FROM promotion WHERE id = :id ";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        return namedParameterJdbcTemplate.queryForObject(
                query,
                params,
                (rs, rowNum) -> Promotion.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .build()
        );
    }

    
    public Optional<Promotion> checkCouponAvailability(int productId, int couponId) {
        String query = "SELECT * FROM promotion_products JOIN promotion ON promotion_products.promotion_id = promotion.id WHERE promotion_products.product_id = :productId AND promotion_products.promotion_id = :couponId";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        params.addValue("couponId", couponId);
    
        List<Promotion> promotions = namedParameterJdbcTemplate.query(
                query,
                params,
                (rs, rowNum) -> Promotion.builder()
                        .id(rs.getInt("id"))
                        .promotion_type(rs.getString("promotion_type"))
                        .name(rs.getString("name"))
                        .discount_type(rs.getString("discount_type"))
                        .discount_value(rs.getInt("discount_value"))
                        .use_started_at(rs.getDate("use_started_at"))
                        .use_ended_at(rs.getDate("use_ended_at"))
                        .build()
        );
    
        return promotions.isEmpty() ? Optional.empty() : Optional.of(promotions.get(0));
    }
}
