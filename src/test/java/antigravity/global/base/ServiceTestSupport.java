package antigravity.global.base;

import antigravity.repository.product.ProductQueryRepository;
import antigravity.repository.promotion.PromotionQueryRepository;
import antigravity.repository.promotionproducts.PromotionProductsQueryRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class ServiceTestSupport {

    @Mock
    protected ProductQueryRepository productQueryRepo;

    @Mock
    protected PromotionProductsQueryRepository promotionProductsQueryRepo;

    @Mock
    protected PromotionQueryRepository promotionQueryRepo;
}
