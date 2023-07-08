package antigravity.repository;

import antigravity.domain.entity.DiscountType;
import antigravity.domain.entity.Money;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PromotionRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<Promotion> promotion = BeanPropertyRowMapper.newInstance(Promotion.class);

    public List<Promotion> getPromotionBy(Integer productId, List<Integer> couponIds) {
        StringBuilder query = new StringBuilder();
        query.append(" SELECT   pm.id, ");
        query.append("          pm.promotion_type, ");
        query.append("          pm.discount_type, ");
        query.append("          pm.discount_value ");
        query.append(" FROM promotion_products pp ");
        query.append("      JOIN promotion pm ON pp.promotion_id = pm.id ");
        query.append("      JOIN product pd ON pp.product_id = pd.id ");
        query.append(" WHERE pp.promotion_id in (:couponIds) ");
        query.append("      AND pp.product_id = :productId ");
        query.append("      AND pm.use_started_at < :now ");
        query.append("      AND :now < pm.use_ended_at ");

        MapSqlParameterSource params = new MapSqlParameterSource("productId", productId);
        params.addValue("couponIds", couponIds);
        params.addValue("now", LocalDateTime.now(ZoneId.of("Asia/Seoul")));

        try {
            return namedParameterJdbcTemplate.query(
                    query.toString(), params,
                    rs -> {
                        List<Promotion> promotions = new ArrayList<>();
                        while(rs.next()) {
                            Promotion promotion = Promotion.builder()
                                    .id(rs.getInt("id"))
                                    .promotionType(PromotionType.valueOf(rs.getString("promotion_type")))
                                    .discountType(DiscountType.valueOf(rs.getString("discount_type")))
                                    .discountValue(new Money(rs.getInt("discount_value")))
                                    .build();
                            promotions.add(promotion);
                        }
                        return promotions;
                    });
        } catch (Exception e) {
            log.error("getPromotionBy >> ", e.getMessage());
            return new ArrayList<>();
        }
    }
}

