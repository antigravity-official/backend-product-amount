package antigravity.global.base;

import antigravity.repository.ProductRepository;
import antigravity.repository.PromotionProductsRepository;
import antigravity.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@Rollback
public abstract class ServiceTestSupport {

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected PromotionProductsRepository promotionProductsRepository;

    @Autowired
    protected PromotionRepository promotionRepository;
}
