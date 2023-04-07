package antigravity.repository.promotion;

import antigravity.domain.promotion.Promotion;
import antigravity.domain.promotion.PromotionRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

import static antigravity.entity.promotion.QPromotion.promotion;
import static antigravity.entity.promotion.QPromotionProducts.promotionProducts;

@Repository
public class QuerydslPromotionRepository extends QuerydslRepositorySupport implements PromotionRepository {
    public QuerydslPromotionRepository() {
        super(Promotion.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Promotion> findByProductIdAndPromotionIdsAndUseAtBetween(long productId, List<Long> promotionIds, LocalDateTime useAt) {
        validation(promotionIds, useAt);
        return from(promotion)
                .select(promotion)
                .join(promotion.promotionProducts, promotionProducts)
                .where(
                        promotion.id.in(promotionIds),
                        promotionProducts.productId.eq(productId),
                        promotion.useStartedAt.loe(useAt),
                        promotion.useEndedAt.goe(useAt)
                )
                .fetch();
    }

    private void validation(List<Long> promotionIds, LocalDateTime useAt) {
        if (promotionIds == null) {
            throw new IllegalArgumentException("promotionIds must not be null");
        }
        if (useAt == null) {
            throw new IllegalArgumentException("useAt must not be null");
        }
    }

    @Override
    @Transactional
    public Promotion save(Promotion promotion) {
        validation(promotion);
        EntityManager entityManager = getEntityManager();
        if (entityManager.contains(promotion)) {
            return entityManager.merge(promotion);
        }
        entityManager.persist(promotion);
        return promotion;
    }

    private void validation(Promotion promotion) {
        if (promotion == null) {
            throw new IllegalArgumentException("promotion must not be null");
        }
    }
}
