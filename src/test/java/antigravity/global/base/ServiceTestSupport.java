package antigravity.global.base;

import antigravity.repository.product.ProductQueryRepository;
import antigravity.repository.promotionproducts.PromotionProductsQueryRepository;
import antigravity.repository.promotion.PromotionQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@Rollback
public abstract class ServiceTestSupport {

    @Autowired
    protected ProductQueryRepository productQueryRepository;

    @Autowired
    protected PromotionProductsQueryRepository promotionProductsQueryRepository;

    @Autowired
    protected PromotionQueryRepository promotionQueryRepository;
}
