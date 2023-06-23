package antigravity.repository;

import antigravity.domain.dto.PromotionProductsDto;
import antigravity.domain.enums.DiscountType;
import antigravity.domain.enums.PromotionType;
import antigravity.exceptions.OmittedRequireFieldException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    public List<PromotionProductsDto> getProductWithPromotions(int productId, int[] promotions){
        String query = """
            SELECT pd.name as product_name, pd.price, promotion_id, product_id, pr.promotion_type, pr.name as promotion_name, pr.discount_type, pr.discount_value, pr.use_started_at, pr.use_ended_at
            FROM PRODUCT pd
                JOIN PROMOTION_PRODUCTS pp
                ON pp.product_id = pd.id
                    JOIN PROMOTION pr
                    ON pp.promotion_id = pr.id
            WHERE pd.id = :productId
            AND pr.id IN (:promotions) 
            """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("productId", productId);
        params.addValue("promotions", Arrays.stream(promotions).boxed().toList());
        return namedParameterJdbcTemplate.query(
            query,
            params,
            (rs, rowNum)-> PromotionProductsDto.builder()
                        .productId(rs.getInt("product_id"))
                        .productName(rs.getString("product_name"))
                        .promotionId(rs.getInt("promotion_id"))
                        .promotionName(rs.getString("promotion_name"))
                        .price(rs.getInt("price"))
                        .promotionType(PromotionType.of(rs.getString("promotion_type")))
                        .discountType(DiscountType.of(rs.getString("discount_type")))
                        .discountValue(rs.getInt("discount_value"))
                        .promotionUseStartedAt(rs.getDate("use_started_at"))
                        .promotionUseEndedAt(rs.getDate("use_ended_at"))
                        .build());
    }

    public Boolean existsProductById(int productId) {
        try {
            String query = """
            SELECT decode(MAX(id) , null, false, true) as flag
                  FROM PRODUCT
                  WHERE id = :productId
            """;

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("productId", productId);

            return namedParameterJdbcTemplate.queryForObject(query,params, (rs, rowNum) -> rs.getBoolean("flag"));

        } catch (EmptyResultDataAccessException e){
            throw new OmittedRequireFieldException("요청하신 상품을 찾을 수 없습니다.");
        }
    }
}
