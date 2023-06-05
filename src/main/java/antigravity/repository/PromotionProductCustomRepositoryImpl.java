package antigravity.repository;

import antigravity.domain.entity.PromotionProducts;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

import static antigravity.domain.entity.QProduct.product;
import static antigravity.domain.entity.QPromotion.promotion;
import static antigravity.domain.entity.QPromotionProducts.promotionProducts;

public class PromotionProductCustomRepositoryImpl implements PromotionProductCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public PromotionProductCustomRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<PromotionProducts> fetch(Integer[] promotionIds, Integer productId) {
        return jpaQueryFactory.selectFrom(promotionProducts)
                .join(promotionProducts.product, product).fetchJoin()
                .join(promotionProducts.promotion, promotion).fetchJoin()
                .where(
                        promotionProducts.promotion.id.in(promotionIds)
                                .and(
                                        promotionProducts.product.id.eq(productId)
                                )
                )
                .fetch();
    }
}
