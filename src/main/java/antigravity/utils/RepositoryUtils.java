package antigravity.utils;

import antigravity.constants.DiscountType;
import antigravity.constants.PromotionType;
import antigravity.domain.entity.Product;
import antigravity.domain.entity.Promotion;
import antigravity.domain.entity.PromotionProducts;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * Repository utility class for mapping rows to entities.
 */
public class RepositoryUtils {

    /**
     * Maps a row in the result set to a Product object.
     *
     * @param rs The ResultSet to map.
     * @param rowNum The number of the current row.
     * @return A Product object corresponding to the current row.
     * @throws SQLException If an SQL error occurs.
     */
    public static Product mapRowToProduct(final ResultSet rs, final Integer rowNum) throws SQLException {
        return Product.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .price(rs.getInt("price"))
                .build();
    }

    /**
     * Maps a row in the result set to a PromotionProducts object.
     *
     * @param rs The ResultSet to map.
     * @param rowNum The number of the current row.
     * @return A PromotionProducts object corresponding to the current row.
     * @throws SQLException If an SQL error occurs.
     */
    public static PromotionProducts mapRowToPromotionProduct(final ResultSet rs, final Integer rowNum) throws SQLException {
        return PromotionProducts.builder()
                .id(rs.getInt("id"))
                .promotionId(rs.getInt("promotion_id"))
                .productId(rs.getInt("product_id"))
                .build();
    }

    /**
     * Maps a row in the result set to a Promotion object.
     *
     * @param rs The ResultSet to map.
     * @param rowNum The number of the current row.
     * @return A Promotion object corresponding to the current row.
     * @throws SQLException If an SQL error occurs.
     */
    public static Promotion mapRowToPromotion(ResultSet rs, Integer rowNum) throws SQLException, IllegalArgumentException {
        Date usedAtDate = rs.getDate("used_at");
        LocalDate usedAt = (usedAtDate != null) ? usedAtDate.toLocalDate() : null;

        return Promotion.builder()
                .id(rs.getInt("id"))
                .promotion_type(PromotionType.valueOf(rs.getString("promotion_type")))
                .name(rs.getString("name"))
                .discount_type(DiscountType.valueOf(rs.getString("discount_type")))
                .discount_value(rs.getInt("discount_value"))
                .use_started_at(rs.getDate("use_started_at").toLocalDate())
                .use_ended_at(rs.getDate("use_ended_at").toLocalDate())
                .used_at(usedAt)
                .build();
    }
}