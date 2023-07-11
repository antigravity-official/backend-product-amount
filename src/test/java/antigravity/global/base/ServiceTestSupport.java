package antigravity.global.base;

import antigravity.repository.product.ProductQueryRepository;
import antigravity.repository.product.ProductRepository;
import antigravity.repository.promotion.PromotionQueryRepository;
import antigravity.repository.promotion.PromotionRepository;
import antigravity.repository.promotionproducts.PromotionProductsQueryRepository;
import antigravity.repository.promotionproducts.PromotionProductsRepository;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
public abstract class ServiceTestSupport {

    @MockBean
    protected ProductQueryRepository productQueryRepo;

    @MockBean
    protected PromotionProductsQueryRepository promotionProductsQueryRepo;

    @MockBean
    protected PromotionQueryRepository promotionQueryRepo;

    @MockBean
    protected ProductRepository productRepo;

    @MockBean
    protected PromotionProductsRepository promotionProductsRepo;

    @MockBean
    protected PromotionRepository promotionRepo;
}
