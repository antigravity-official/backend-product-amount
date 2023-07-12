package antigravity.global.base;

import antigravity.repository.product.ProductQueryRepository;
import antigravity.repository.product.ProductRepository;
import antigravity.repository.promotion.PromotionQueryRepository;
import antigravity.repository.promotion.PromotionRepository;
import antigravity.repository.promotionproducts.PromotionProductsQueryRepository;
import antigravity.repository.promotionproducts.PromotionProductsRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;

@ExtendWith(MockitoExtension.class)
public abstract class ServiceTestSupport {

    @Mock
    protected ProductQueryRepository productQueryRepo;

    @Mock
    protected PromotionProductsQueryRepository promotionProductsQueryRepo;

    @Mock
    protected PromotionQueryRepository promotionQueryRepo;

    @Mock
    protected ProductRepository productRepo;

    @Mock
    protected PromotionProductsRepository promotionProductsRepo;

    @Mock
    protected PromotionRepository promotionRepo;
}
