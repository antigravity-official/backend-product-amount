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

    @Override
    @Transactional
    public Promotion save(Promotion promotion) {
        EntityManager entityManager = getEntityManager();
        if (entityManager.contains(promotion)) {
            return entityManager.merge(promotion);
        }
        entityManager.persist(promotion);
        return promotion;
    }
}
