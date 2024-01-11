package antigravity.repository;

import antigravity.domain.entity.Promotion;
import antigravity.utils.RepositoryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Repository for performing database operations on Promotion entities.
 */
@RequiredArgsConstructor
@Repository
public class PromotionRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Retrieves a list of Promotions based on their IDs.
     *
     * @param ids The IDs of the promotions to retrieve.
     * @return A List of Promotions corresponding to the provided IDs.
     */
    public List<Promotion> getPromotion(List<Integer> ids) {
        final String query = "SELECT * FROM `promotion` WHERE id IN (:ids)";
        final MapSqlParameterSource params = new MapSqlParameterSource("ids", ids);

        return jdbcTemplate.query(
                query,
                params,
                RepositoryUtils::mapRowToPromotion
        );
    }

    /**
     * Updates the 'used_at' field of Promotions for a list of given IDs to the current date.
     *
     * @param ids The IDs of the promotions to be updated.
     * @return The number of rows affected by the update.
     */
    public int updatePromotionUsedAt(List<Integer> ids) {
        final String query = "UPDATE `promotion` SET used_at = :usedAt WHERE id IN (:ids)";
        final MapSqlParameterSource params = new MapSqlParameterSource(Map.of(
                "usedAt", Date.valueOf(LocalDate.now()),
                "ids", ids
        ));

        return jdbcTemplate.update(query, params);
    }
}