package antigravity.repository.promotion;

import antigravity.domain.promotion.Promotion;
import antigravity.domain.promotion.PromotionRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static antigravity.entity.promotion.QPromotion.promotion;
import static antigravity.entity.promotion.QPromotionProducts.promotionProducts;

@Repository
@RequiredArgsConstructor
public class QuerydslPromotionRepository implements PromotionRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional(readOnly = true)
    public List<Promotion> findByProductIdAndPromotionIdsAndUseAtBetween(long productId, List<Long> promotionIds, LocalDateTime useAt) {
        return jpaQueryFactory
                .selectFrom(promotion)
                .join(promotion.promotionProducts, promotionProducts)
                .where(
                    promotion.id.in(promotionIds),
                    promotionProducts.productId.eq(productId),
                    promotion.useStartedAt.loe(useAt),
                    promotion.useEndedAt.goe(useAt)
                )
                .fetch();
    }
}
